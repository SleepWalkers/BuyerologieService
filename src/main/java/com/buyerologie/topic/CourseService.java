package com.buyerologie.topic;

import java.util.List;

import com.buyerologie.common.exception.PageNotFoundException;
import com.buyerologie.topic.exception.TopicException;
import com.buyerologie.topic.vo.Course;

public interface CourseService {

    Course getCourse(int courseId) throws PageNotFoundException;

    List<Course> getCourses(int page, int pageSize);

    List<String> getCourseVideoIds(List<Integer> courseIds);

    int count();

    int getCourseId(String videoId);

    void createCourse(int creatorId, String title, String videoId, String content)
                                                                                  throws TopicException;

    void editCourse(int id, String title, String videoId, String content) throws TopicException;

    void deleteCourse(int courseId);
}
