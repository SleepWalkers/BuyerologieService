package com.buyerologie.video.imp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.buyerologie.sdk.polyv.com.polyv.sdk.PolyvSDKClient;
import com.buyerologie.topic.CourseService;
import com.buyerologie.utils.SetUtil;
import com.buyerologie.utils.StringUtil;
import com.buyerologie.video.VideoService;
import com.buyerologie.video.dao.PlayListDao;
import com.buyerologie.video.dao.PlayListVideoRelationDao;
import com.buyerologie.video.dao.VideoDao;
import com.buyerologie.video.model.PlayList;
import com.buyerologie.video.model.PlayListVideoRelation;
import com.buyerologie.video.model.Video;
import com.buyerologie.video.vo.DetailedPlayList;
import com.buyerologie.video.vo.ListVideo;

@Service("videoService")
public class VideoServiceImp implements VideoService {

    @Resource
    private VideoDao                 videoDao;

    @Resource
    private PlayListDao              playListDao;

    @Resource
    private PlayListVideoRelationDao playListVideoRelationDao;

    @Resource
    private CourseService            courseService;

    private static final String      READ_TOKEN        = "0c26c241-0187-4daa-b7c4-cc98527a9f91";

    private static final String      WRITE_TOKEN       = "d9698f5b-4c47-4899-9348-c42f87c05a12";

    private static final String      SECRET_KEY        = "Tw5ZYR6sdE";

    private static final String      USER_ID           = "9b98637174";

    private static final int         DEFAULT_PAGE_SIZE = 20;

    private static final Logger      logger            = Logger.getLogger(VideoServiceImp.class);

    @Override
    public List<Video> getAllVideos() {
        return videoDao.selectAll();
    }

    @Override
    public void sync() {
        int page = 1;
        JSONArray playListArray = null;

        Set<Long> newPlayListIdSet = new HashSet<>();
        do {
            playListArray = getPolyvSDKClient().getPlayList(page, DEFAULT_PAGE_SIZE);
            if (playListArray == null) {
                return;
            }
            for (Iterator<Object> iterator = playListArray.iterator(); iterator.hasNext();) {
                JSONObject obj = (JSONObject) iterator.next();

                PlayList playList = playListDao.selectById(obj.getLongValue("videoid"));
                if (playList == null) {
                    playListDao.insert(new PlayList(obj.getLongValue("videoid"), obj
                        .getString("title")));
                } else {
                    playList.setTitle(obj.getString("title"));
                    playListDao.updateById(playList);
                }

                insertVideo(obj.getLongValue("videoid"));
                newPlayListIdSet.add(obj.getLongValue("videoid"));
            }
            page++;

        } while (playListArray != null);

        List<Long> playListIds = playListDao.getAllPlayListId();
        if (playListIds == null || playListIds.isEmpty()) {
            return;
        }
        Set<Long> originalPlayListIdSet = new HashSet<>(playListIds);

        Set<Long> needDeletePlayListIdSet = SetUtil.difference(originalPlayListIdSet,
            newPlayListIdSet);
        if (needDeletePlayListIdSet == null || needDeletePlayListIdSet.isEmpty()) {
            return;
        }
        for (long needDeletePlayListId : needDeletePlayListIdSet) {
            playListDao.deleteById(needDeletePlayListId);
            playListVideoRelationDao.deleteByPlayListId(needDeletePlayListId);
        }
    }

    private void insertVideo(long playListId) {
        JSONArray playList = getPolyvSDKClient().getPlayList(playListId);

        if (playList == null) {
            return;
        }
        JSONArray videoJsonArray = ((JSONObject) (playList.get(0))).getJSONArray("videolist");
        if (videoJsonArray == null || videoJsonArray.isEmpty()) {
            playListVideoRelationDao.deleteByPlayListId(playListId);
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Set<String> newPlayListVideoIdSet = new HashSet<>();

        playListVideoRelationDao.deleteByPlayListId(playListId);
        try {
            for (Iterator<Object> videoIterator = videoJsonArray.iterator(); videoIterator
                .hasNext();) {
                JSONObject videoObj = (JSONObject) videoIterator.next();
                inserOrUpdate(playListId, videoObj.getString("vid"), videoObj.getString("title"),
                    sdf.parse(videoObj.getString("duration")));

                newPlayListVideoIdSet.add(videoObj.getString("vid"));
            }
        } catch (ParseException e) {
            logger.error("", e);
        }
        List<String> videoIdList = playListVideoRelationDao.selectVideoListByPlayListId(playListId);
        if (videoIdList == null || videoIdList.isEmpty()) {
            return;
        }
        Set<String> originalVideoIdSet = new HashSet<>(videoIdList);

        Set<String> needDeleteVideoIdSet = SetUtil.difference(originalVideoIdSet,
            newPlayListVideoIdSet);
        if (needDeleteVideoIdSet == null || needDeleteVideoIdSet.isEmpty()) {
            return;
        }

        for (String needDeleteVideoId : needDeleteVideoIdSet) {
            playListVideoRelationDao.deleteByPlayListIdAndVideoId(playListId, needDeleteVideoId);
        }
    }

    private void inserOrUpdate(long playListId, String videoId, String title, Date duration) {
        Video video = videoDao.selectById(videoId);
        if (video == null) {
            videoDao.insert(new Video(videoId, title, duration));
        } else {
            video.setTitle(title);
            video.setDuration(duration);
            videoDao.updateById(video);
        }

        if (playListVideoRelationDao.selectByPlayListIdAndVideoId(playListId, videoId) == null) {
            playListVideoRelationDao.insert(new PlayListVideoRelation(playListId, videoId));
        }
    }

    private PolyvSDKClient getPolyvSDKClient() {
        PolyvSDKClient client = PolyvSDKClient.getInstance();
        client.setReadtoken(READ_TOKEN);
        client.setWritetoken(WRITE_TOKEN);
        client.setSecretkey(SECRET_KEY);
        client.setUserid(USER_ID);
        return client;
    }

    @Override
    public DetailedPlayList getDetailedPlayList(String videoId) {
        if (StringUtils.isBlank(videoId)) {
            return null;
        }
        long playListId = playListVideoRelationDao.selectByVideoId(videoId);

        return getDetailedPlayList(playListId);
    }

    @Override
    public DetailedPlayList getDetailedPlayList(long playListId) {
        if (playListId <= 0) {
            return null;
        }
        PlayList playList = playListDao.selectById(playListId);
        if (playList == null) {
            return null;
        }
        return new DetailedPlayList(playList, getListVideos(playListId));
    }

    private List<ListVideo> getListVideos(long playListId) {
        List<Video> videos = videoDao.selectByPlayList(playListId);
        return buildListVideos(playListId, videos);
    }

    private List<ListVideo> buildListVideos(long playListId, List<Video> videos) {
        if (videos == null || videos.isEmpty()) {
            return null;
        }
        List<ListVideo> listVideos = new ArrayList<>();
        for (Video video : videos) {
            int courseId = courseService.getCourseId(video.getId());
            if (courseId == 0) {
                continue;
            }
            listVideos.add(new ListVideo(courseId, playListId, video));
        }
        return listVideos;
    }

    @Override
    public List<DetailedPlayList> getAllDetailedPlayLists() {
        List<PlayList> playLists = playListDao.getAll();
        if (playLists == null || playLists.isEmpty()) {
            return null;
        }
        List<DetailedPlayList> detailedPlayLists = new ArrayList<>();

        for (PlayList playList : playLists) {
            detailedPlayLists.add(new DetailedPlayList(playList, getListVideos(playList.getId())));
        }
        return detailedPlayLists;
    }

    @Override
    public List<PlayList> getAll() {
        return playListDao.getAll();
    }

    public static void main(String[] args) {
        List<String> videoIds = new ArrayList<>();
        videoIds.add("123");
        videoIds.add("513");
        System.out.println(StringUtil.buildStringListToString(videoIds, "\",\"").substring(1)
                           + "\"");
    }

    @Override
    public List<ListVideo> getListVideos(List<String> videoIds) {
        if (videoIds == null || videoIds.isEmpty()) {
            return null;
        }

        List<Video> videos = videoDao.selectByIds(StringUtil.buildStringListToString(videoIds,
            "\",\"").substring(1)
                                                  + "\"");
        if (videos == null || videos.isEmpty()) {
            return null;
        }
        List<ListVideo> listVideos = new ArrayList<>();
        for (Video video : videos) {
            int courseId = courseService.getCourseId(video.getId());
            if (courseId == 0) {
                continue;
            }
            listVideos.add(new ListVideo(courseId, playListVideoRelationDao.selectByVideoId(video
                .getId()), video));
        }

        return listVideos;
    }
}
