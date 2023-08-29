package com.hang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.entity.Address;
import com.hang.mapper.AddressMapper;
import com.hang.service.AddressService;
import org.springframework.stereotype.Service;

/**
 * (Address)表服务实现类
 *
 * @author makejava
 * @since 2023-08-29 16:10:04
 */
@Service("addressService")
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

}

