package com.testpart1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.testpart1.entity.PPart;
import com.testpart1.mapper.PPartMapper;
import com.testpart1.service.PPartService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Planet
 * @since 2022-03-16
 */
@Service
public class PPartServiceImpl extends ServiceImpl<PPartMapper, PPart> implements PPartService {

}
