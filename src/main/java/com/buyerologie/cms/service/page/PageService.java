package com.buyerologie.cms.service.page;

import java.util.List;

import com.buyerologie.cms.service.vo.PageVO;

public interface PageService {

    /**
     * 获得简单的页面VO 只有页面的信息
     * @param pageId
     * @return
     */
    public PageVO getPage(int pageId);

    public PageVO getPage(String name);

    /**
     * 获得所有页面的简单VO 只有页面信息 
     * @return
     */
    public List<PageVO> getPageVOs();

    /**
     * 新增页面
     * @param pageName      页面名
     * @param pageEnName    页面英文名
     * @param title         
     * @param description   
     * @param keywords
     */
    public void publishPage(String pageName, String pageEnName, String title, String description,
                            String keywords);

    /**
     * 编辑页面
     * @param pageId
     * @param pageName
     * @param pageEnName
     * @param title
     * @param description
     * @param keywords
     */
    public void editPage(int pageId, String pageName, String pageEnName, String title,
                         String description, String keywords);

    /**
     * 删除页面
     * @param pageId
     */
    public void deletePage(int pageId);

    /**
     * 统计所有页面数
     * @return
     */
    public int countPage();
}
