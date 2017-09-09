package com.reps.jifen.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.jifen.entity.JfPointsExchange;
import com.reps.jifen.repository.IJfPointsExchangeRepository;
import com.reps.jifen.service.IJfPointsExchangeService;

@Service
public class JfPointsExchangeServiceImpl implements IJfPointsExchangeService{
	
	protected final Logger logger = LoggerFactory.getLogger(JfPointsExchangeServiceImpl.class);
	
	@Autowired
	private IJfPointsExchangeRepository repository;

	@Override
	public ListResult<JfPointsExchange> findByPersonId(String personId, Integer pageIndex, Integer pageSize) throws RepsException{
		if(StringUtil.isBlank(personId)){
			throw new RepsException("人员ID不能为空");
		}
		//设置分页参数
		pageIndex = null == pageIndex ? 1 : pageIndex;
		pageSize = null == pageSize ? 20 : pageSize;
		//构建分页对象
		PageRequest pageRequest = new PageRequest(pageSize * (pageIndex - 1), pageSize);
		Page<JfPointsExchange> page = repository.findByPersonId(personId, pageRequest);
		if(null == page) {
			throw new RepsException("mongodb查询失败");
		}
		ListResult<JfPointsExchange> listResult = new ListResult<>(page.getContent(), page.getTotalElements());
		listResult.setPageSize(pageSize);
		return listResult;
	}

}
