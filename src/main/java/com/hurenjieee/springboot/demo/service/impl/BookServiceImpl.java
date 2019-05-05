package com.hurenjieee.springboot.demo.service.impl;

import com.hurenjieee.springboot.demo.entity.Book;
import com.hurenjieee.springboot.demo.mapper.BookMapper;
import com.hurenjieee.springboot.demo.service.IBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jack
 * @since 2019-03-19
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

}
