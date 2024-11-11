package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.ProhibitedWords;
import edu.hnu.conference_system.service.ProhibitedWordsService;
import edu.hnu.conference_system.mapper.ProhibitedWordsMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【prohibited_words】的数据库操作Service实现
* @createDate 2024-11-11 19:10:49
*/
@Service
public class ProhibitedWordsServiceImpl extends ServiceImpl<ProhibitedWordsMapper, ProhibitedWords>
    implements ProhibitedWordsService{

}




