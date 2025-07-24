package com.atguigu.lease.web.admin.schedule;


import com.atguigu.lease.model.entity.LeaseAgreement;
import com.atguigu.lease.model.enums.LeaseStatus;
import com.atguigu.lease.web.admin.service.LeaseAgreementService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleTasks {

//    @Scheduled(cron = "******")
//    public void test()
//    }

    @Autowired
    private LeaseAgreementService service;

    //TODO 定时任务中的corn表达式的应用！！！
    //这里表示每天的0点检查租约状态
    @Scheduled(cron = "0 0 0 * * *")
    public void checkLeaseStatus(){
        //updateWrapper中的le方法是小于等于


        LambdaUpdateWrapper<LeaseAgreement> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.le(LeaseAgreement::getLeaseEndDate,new Date());
        updateWrapper.in(LeaseAgreement::getStatus,LeaseStatus.WITHDRAWING,LeaseStatus.SIGNED);
        updateWrapper.set(LeaseAgreement::getStatus, LeaseStatus.EXPIRED);

        service.update(updateWrapper);
    }
}
