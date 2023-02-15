package com.yahoo.service.inter;

import com.yahoo.pojo.vo.AnnouncementVO;
import org.springframework.amqp.core.Message;

public interface AnnouncementService {
    void sendMsg(AnnouncementVO msg);
    AnnouncementVO getMsg();
}
