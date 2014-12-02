package com.mofang.framework.data.mysql.core.criterion.operand;

/**
 * 
 * @author zhaodx
 *
 */
public class NoneOperand extends Operand
{
	@Override
	protected String toExpression()
	{
		return "";
	}
}