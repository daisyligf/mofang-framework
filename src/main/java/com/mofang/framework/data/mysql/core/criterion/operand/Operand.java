package com.mofang.framework.data.mysql.core.criterion.operand;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zhaodx
 *
 */
public abstract class Operand
{
	protected List<Operand> operands = new ArrayList<Operand>();
	
	public Operand append(Operand operand) throws Exception
	{
		if(null == operand)
			throw new Exception("operand can not be null.");
		
		operands.add(operand);
		return this;
	}
	
	public String toString()
	{
		if (null == operands || operands.size() == 0)
            return this.toExpression();

        StringBuilder expr = new StringBuilder();
        expr.append(this.toExpression());
        for (Operand operand : operands)
            expr.append(operand.toString());

        return expr.toString();
	}
	
	protected abstract String toExpression();
}