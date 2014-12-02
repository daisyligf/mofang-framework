package com.mofang.framework.data.mysql.core.criterion.operand;

/**
 * 
 * @author zhaodx
 *
 */
public class WhereOperand extends Operand
{
	@Override
	protected String toExpression()
	{
		return "WHERE ";
	}
}