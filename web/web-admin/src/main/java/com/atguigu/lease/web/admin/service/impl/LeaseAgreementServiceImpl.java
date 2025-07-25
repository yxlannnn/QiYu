package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.web.admin.mapper.*;
import com.atguigu.lease.web.admin.service.LeaseAgreementService;
import com.atguigu.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.atguigu.lease.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    @Autowired
    private LeaseAgreementMapper leaseAgreementMapper;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private RoomInfoMapper roomInfoMapper;
    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    @Autowired
    private LeaseTermMapper leaseTermMapper;



    @Override
    public AgreementVo getAgreementById(Long id) {

        //TODO 直接在service层完成数据库查询
        //其实查询的本质就是将所需要的字段全部查出来然后打包在一个容器里返回给前端
        //AgreementVo中的字段有四个自己的字段，且都是引用类型

        LeaseAgreement leaseAgreement = leaseAgreementMapper.selectById(id);

        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(leaseAgreement.getApartmentId());

        RoomInfo roomInfo = roomInfoMapper.selectById(leaseAgreement.getRoomId());

        PaymentType paymentType = paymentTypeMapper.selectById(leaseAgreement.getPaymentTypeId());

        LeaseTerm leaseTerm = leaseTermMapper.selectById(leaseAgreement.getLeaseTermId());


        AgreementVo agreementVo = new AgreementVo();
        BeanUtils.copyProperties(leaseAgreement,agreementVo);
        agreementVo.setApartmentInfo(apartmentInfo);
        agreementVo.setRoomInfo(roomInfo);
        agreementVo.setPaymentType(paymentType);
        agreementVo.setLeaseTerm(leaseTerm);

        return  agreementVo;


    }

    @Override
    public IPage<AgreementVo> pageAgreement(Page<AgreementVo> page, AgreementQueryVo queryVo) {
        return leaseAgreementMapper.pageAgreement(page,queryVo);
    }
}




