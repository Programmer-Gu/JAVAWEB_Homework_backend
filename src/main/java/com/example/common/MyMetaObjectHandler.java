package com.example.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.function.Function;

/**
 * 自定义元数据对象处理器
 */

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    private final HashMap<String, Function<MetaObject,Void>> functionHash;

    public MyMetaObjectHandler() {
        super();
        functionHash = new HashMap<>();
//        functionHash.put(User.class.getSimpleName(), UserAutoInsert());
//        functionHash.put(GptConversation.class.getSimpleName(),ConversationAutoInsert());
//        functionHash.put(GptHistory.class.getSimpleName(), GptHistoryAutoInsert() );
    }

    /**
     * 插入操作自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充【insert】");
        log.info(metaObject.toString());

//        functionHash.get(metaObject.getOriginalObject().getClass().getSimpleName())
//                    .apply(metaObject);
    }

    /**
     * 更新操作自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【update】");
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();
        log.info("线程id为:{}", id);

        metaObject.setValue("lastLoginTime", LocalDateTime.now());
    }

//
//    /**
//     * 用户实体类自动填充
//     * @return
//     */
//    public Function<MetaObject,Void> UserAutoInsert() {
//        return (metaObject) -> {
//            log.info("用户实体自动插入");
//            metaObject.setValue("createTime", LocalDateTime.now());
//            metaObject.setValue("lastLoginTime", LocalDateTime.now());
//            metaObject.setValue("authority", (short) 1);
//            metaObject.setValue("avatar", "https://every-ai-test.oss-cn-nanjing.aliyuncs.com/defaultHeadPicture/%E5%A4%B4%E5%83%8F%20%E7%AE%80%E7%BA%A6%E7%94%B7%E5%A3%AB.svg");
//            metaObject.setValue("nickname", "未命名");
//            return null;
//        };
//    }
//
//    /**
//     * 聊天实体自动填充
//     * @return
//     */
//    public Function<MetaObject,Void> ConversationAutoInsert() {
//        return (metaObject) -> {
//            log.info("聊天实体自动插入");
//            metaObject.setValue("timestamp", LocalDateTime.now());
//            return null;
//        };
//    }
//
//    public Function<MetaObject,Void> GptHistoryAutoInsert() {
//        return (metaObject) -> {
//            log.info("对话实体自动插入");
//            metaObject.setValue("createTime", LocalDateTime.now());
//            return null;
//        };
//    }
}
