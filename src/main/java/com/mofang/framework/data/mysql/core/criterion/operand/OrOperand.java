package com.mofang.framework.data.mysql.core.criterion.operand;

/**
 * 
 * @author zhaodx
 *
 */
public class OrOperand extends Operand
{
	@Override
	protected String toExpression()
	{
		return " OR ";
	}
}