package edu.hnu.conference_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hnu.conference_system.domain.Record;
import edu.hnu.conference_system.service.RecordService;
import edu.hnu.conference_system.mapper.RecordMapper;
import edu.hnu.conference_system.utils.TxtUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
* @author lenovo
* @description 针对表【record】的数据库操作Service实现
* @createDate 2024-11-11 18:59:03
*/
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record>
    implements RecordService{

    @Resource
    private RecordMapper recordMapper;

    @Override
    public String getRecordById(Long meetingRecordId) throws IOException {
        String txtPath =  recordMapper.selectOne(
                new QueryWrapper<Record>().eq("record_id",meetingRecordId)
        ).getRecordPath();

        return TxtUtils.readTxt(txtPath);
    }
}




