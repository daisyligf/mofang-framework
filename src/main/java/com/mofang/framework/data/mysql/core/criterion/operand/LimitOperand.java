package com.mofang.framework.data.mysql.core.criterion.operand;

/**
 * 
 * @author zhaodx
 *
 */
public class LimitOperand extends Operand
{
	private Long start;
	private Long end;
	
	public LimitOperand(Long start, Long end)
	{
		this.start = start;
		this.end = end;
	}

	@Override
	protected String toExpression()
	{
		StringBuilder strLimit = new StringBuilder();
		if(null != start)
			strLimit.append("," + start);
		if(null != end)
			strLimit.append("," + end);
		
		String limit = strLimit.toString();
		if(limit.length() > 0)
			limit = limit.substring(1);
		
		return String.format("limit %s ", limit);
	}
}