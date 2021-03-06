package com.reps.jifen.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reps.core.restful.RestBaseController;
import com.reps.core.restful.RestResponse;
import com.reps.core.restful.RestResponseStatus;
import com.reps.jifen.entity.PointLevel;
import com.reps.jifen.service.IPointLevelService;
import com.reps.jifen.util.LevelUtil;

/**
 * 积分等级
 * @author qianguobing
 * @date 2017年9月9日 下午2:59:37
 */
@RestController
@RequestMapping(value = "/uapi/pointlevel")
public class PointLevelRest  extends RestBaseController{
	
	protected final Logger logger = LoggerFactory.getLogger(PointLevelRest.class);
	
	@Autowired
	private IPointLevelService jfPointLevelService;
	
	@RequestMapping(value = "/list")
	public RestResponse<List<PointLevel>> list() {
		try {
			List<PointLevel> result = jfPointLevelService.queryAll(null);
			return wrap(RestResponseStatus.OK, "查询成功", result);
		} catch (Exception e) {
			logger.error("查询异常", e);
			return wrap(RestResponseStatus.INTERNAL_SERVER_ERROR, "查询异常：" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getlevel")
	public RestResponse<Integer> getLevel(Integer points) {
		RestResponse<Integer> result = new RestResponse<>();
		try {
			if (points == null) {
				result.setStatus(RestResponseStatus.INTERNAL_SERVER_ERROR.code());
				result.setMessage("请求参数错误");
				return result;
			}
			List<PointLevel> list = jfPointLevelService.queryAll(null);
			result.setResult(LevelUtil.getPointsLevel(points, list));
			result.setStatus(RestResponseStatus.OK.code());
			result.setMessage("查询成功");
		} catch (Exception e) {
			logger.error("查询异常", e);
			return wrap(RestResponseStatus.INTERNAL_SERVER_ERROR, "查询异常：" + e.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取升级所需积分数
	 * @param points
	 * @return
	 */
	@RequestMapping(value = "/getlvpoints")
	public RestResponse<Integer> getlvpoints(Integer points, Integer level) {
		RestResponse<Integer> result = new RestResponse<>();
		try {
			if (points == null || level == null) {
				result.setStatus(RestResponseStatus.INTERNAL_SERVER_ERROR.code());
				result.setMessage("请求参数错误");
				return result;
			}
			PointLevel data = jfPointLevelService.findByLevel(level + 1);
			if (data == null) {
				return wrap(RestResponseStatus.OK, "查询成功", 0);
			}
			return wrap(RestResponseStatus.OK, "查询成功", data.getPoint() - points);
		} catch (Exception e) {
			logger.error("查询异常", e);
			return wrap(RestResponseStatus.INTERNAL_SERVER_ERROR, "查询异常：" + e.getMessage());
		}
	}
	
}
