package com.mofang.framework.data.mysql.core.criterion.operand;

import com.mofang.framework.data.mysql.core.criterion.type.BracketType;

/**
 * 
 * @author zhaodx
 *
 */
public class BracketOperand extends Operand
{
	private BracketType bracketType;
	
	public BracketOperand(BracketType bracketType)
	{
		this.bracketType = bracketType;
	}

	@Override
	protected String toExpression() 
	{
		switch (bracketType)
        {
            case Left:
                return "(";
            case Right:
                return ")";
            default:
                return "";
        }
	}
}