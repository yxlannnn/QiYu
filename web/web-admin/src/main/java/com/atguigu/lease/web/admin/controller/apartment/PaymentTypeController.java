package com.atguigu.lease.web.admin.controller.apartment;


import com.atguigu.lease.common.result.Result;
import com.atguigu.lease.model.entity.PaymentType;
import com.atguigu.lease.web.admin.service.PaymentTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "支付方式管理")
@RequestMapping("/admin/payment")
@RestController
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService service;

    @Operation(summary = "查询全部支付方式列表")
    @GetMapping("list")
    public Result<List<PaymentType>> listPaymentType() {
        //TODO 查询全部支付方式列表
        //用service的list方法直接查询
        //
        //存在问题：要过滤逻辑删除字段为 1 的数据
        //
        //方法一：给yml文件做全局配置
        //mybatis-plus:
        //  global-config:
        //    db-config:
        //      logic-delete-field: is_delete  #对应数据库中表示逻辑删除的字段
        //      logic-delete-value: 1  #表示逻辑删除成功的值
        //      logic-not-delete-value: 0  #表示逻辑未删除的
        //
        //方法二：直接给isdelete字段加@TableLogic注解
        //
        //两种方法二选一

        //注意事项：逻辑删除功能只对Mybatis-Plus自动注入的sql起效，也就是说，对于手动在`Mapper.xml`文件配置的sql不会生效，需要单独考虑。
        List<PaymentType> list = service.list();
        return Result.ok(list);
    }

    @Operation(summary = "保存或更新支付方式")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdatePaymentType(@RequestBody PaymentType paymentType) {

        //TODO 保存或更新支付方式
        service.saveOrUpdate(paymentType);

        return Result.ok();
    }

    @Operation(summary = "根据ID删除支付方式")
    @DeleteMapping("deleteById")
    public Result deletePaymentById(@RequestParam Long id) {

        //TODO 根据ID删除支付方式
        service.removeById(id);

        return Result.ok();
    }

}















