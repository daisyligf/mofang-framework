package com.mofang.framework.data.mysql;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.mofang.framework.data.mysql.core.meta.MysqlParameter;
import com.mofang.framework.data.mysql.core.meta.ResultData;

/**
 * SQL执行器，支持异步调用
 * @author zhaodx
 *
 */
public class MysqlExecutor
{
	private final static MysqlExecutor MYSQL = new MysqlExecutor();
	private final static ExecutorService Executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);
	
	private MysqlExecutor()
	{}
	
	public static MysqlExecutor getInstance()
	{
		return MYSQL;
	}
	
	public ResultData execute(Connection conn, MysqlParameter parameter) throws Exception
	{
		return MysqlHelper.execute(conn, parameter);
	}
	
	public ResultData execute(Connection conn, MysqlParameter parameter, boolean needAutoIncrement) throws Exception
	{
		return MysqlHelper.execute(conn, parameter, needAutoIncrement);
	}
	
	public ResultData executeTransaction(Connection conn, List<MysqlParameter> parameterList) throws Exception
	{
		return MysqlHelper.executeTransaction(conn, parameterList);
	}

	public ResultData query(Connection conn, MysqlParameter parameter) throws Exception
	{
		return MysqlHelper.query(conn, parameter);
	}
	
	public Future<ResultData> invokeExecute(Connection conn, MysqlParameter parameter) throws Exception
	{
		Callable<ResultData> task = new MysqlInvokeExecute(conn, parameter, false);
		return Executor.submit(task);
	}
	
	public Future<ResultData> invokeExecute(Connection conn, MysqlParameter parameter, boolean needAutoIncrement) throws Exception
	{
		Callable<ResultData> task = new MysqlInvokeExecute(conn, parameter, needAutoIncrement);
		return Executor.submit(task);
	}
	
	public Future<ResultData> invokeExecuteTransaction(Connection conn, List<MysqlParameter> parameterList) throws Exception
	{
		Callable<ResultData> task = new MysqlInvokeExecuteTransaction(conn, parameterList);
		return Executor.submit(task);
	}
	
	public Future<ResultData> invokeQuery(Connection conn, MysqlParameter parameter) throws Exception
	{
		Callable<ResultData> task = new MysqlInvokeQuery(conn, parameter);
		return Executor.submit(task);
	}
	
	class MysqlInvokeExecute implements Callable<ResultData>
	{
		private Connection conn; 
		private MysqlParameter parameter;
		private boolean needAutoIncrement;
		
		public MysqlInvokeExecute(Connection conn, MysqlParameter parameter, boolean needAutoIncrement)
		{
			this.conn = conn;
			this.parameter = parameter;
			this.needAutoIncrement = needAutoIncrement;
		}
		
		@Override
		public ResultData call() throws Exception
		{
			return MysqlHelper.execute(conn, parameter, needAutoIncrement);
		}
	}
	
	class MysqlInvokeExecuteTransaction implements Callable<ResultData>
	{
		private Connection conn; 
		private List<MysqlParameter> parameterList;
		
		public MysqlInvokeExecuteTransaction(Connection conn, List<MysqlParameter> parameterList)
		{
			this.conn = conn;
			this.parameterList = parameterList;
		}
		
		@Override
		public ResultData call() throws Exception
		{
			return MysqlHelper.executeTransaction(conn, parameterList);
		}
	}

	class MysqlInvokeQuery implements Callable<ResultData>
	{
		private Connection conn; 
		private MysqlParameter parameter;
	
		public MysqlInvokeQuery(Connection conn, MysqlParameter parameter)
		{
			this.conn = conn;
			this.parameter = parameter;
		}
		
		@Override
		public ResultData call() throws Exception
		{
			return MysqlHelper.query(conn, parameter);
		}
	}
}