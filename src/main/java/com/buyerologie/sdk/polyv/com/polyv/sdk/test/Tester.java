package com.buyerologie.sdk.polyv.com.polyv.sdk.test;

import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.buyerologie.sdk.polyv.com.polyv.sdk.PolyvSDKClient;
import com.buyerologie.sdk.polyv.com.polyv.sdk.Video;

public class Tester {
    public static void main(String[] args) {

        PolyvSDKClient client = PolyvSDKClient.getInstance();
        client.setReadtoken("0c26c241-0187-4daa-b7c4-cc98527a9f91");
        client.setWritetoken("d9698f5b-4c47-4899-9348-c42f87c05a12");
        client.setSecretkey("Tw5ZYR6sdE");
        client.setUserid("9b98637174");
        JSONArray jsonArray = PolyvSDKClient.getInstance().getPlayList(1481277802186L);

        //Video video = PolyvSDKClient.getInstance().getVideo("9b986371749aa39d26ecc7b66c063c83_9");
        testListPlay();
        //testResumableUpload();
        // TODO Auto-generated method stub
        //testUpload();
        //testResumableUpload();
        //testDeleteVideo();
    }

    /**
     * 断点续传上传实例
     */
    //    public static void testResumableUpload() {
    //
    //        PolyvSDKClient client = PolyvSDKClient.getInstance();
    //
    //        String vid = "";
    //        try {
    //            vid = client.resumableUpload("/Users/hhl/Desktop/videos/名字abc.mp4", "标题11", "", "", 1,
    //                new Progress() {
    //                    @Override
    //                    public void run(long offset, long max) {
    //                        // TODO Auto-generated method stub
    //                        int percent = (int) (offset * 100 / max);
    //                        System.out.println(percent);
    //
    //                    }
    //
    //                });
    //        } catch (Exception e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    //
    //        System.out.println(vid);
    //    }

    public static void testGet() {
        try {
            Video v = PolyvSDKClient.getInstance().getVideo("sl8da4jjbx33489b03e87dd99641901d_s");
            System.out.println(v.getDuration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testUpload() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Video v;
                try {
                    v = PolyvSDKClient.getInstance().upload("/Users/hhl/Downloads/test.avi",
                        "我的标题", "tag", "desc", 0);
                    System.out.println(v.getFirstImage());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        t.start();

        while (true) {
            int percent = PolyvSDKClient.getInstance().getPercent();
            if (percent == 100) {
                break;
            }
            System.out.println("upload percent: " + percent + "%");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void testDeleteVideo() {
        try {

            boolean result = PolyvSDKClient.getInstance().deleteVideo(
                "sl8da4jjbxa1077082a56e35adef93c4_s");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testListPlay() {
        try {

            JSONArray list = PolyvSDKClient.getInstance().getPlayList(1, 20);
            for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
                JSONObject obj = (JSONObject) iterator.next();
                System.out.println((long) obj.get("videoid"));
            }
            System.out.println("----查看结束----");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testListVideo() {
        try {
            List<Video> list = PolyvSDKClient.getInstance().getVideoList(1, 20);
            for (int i = 0; i < list.size(); i++) {
                Video v = list.get(i);
                System.out.println(v.getVid() + "/" + v.getTitle());
            }
            System.out.println("----查看结束----");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
