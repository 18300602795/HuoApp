package com.huosuapp.Bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommentBean implements Serializable {
	private static final long serialVersionUID = -1;
	private int code;

	private String msg;

	private Comment data;

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setData(Comment data) {
		this.data = data;
	}

	public Comment getData() {
		return this.data;
	}

	public static class Comment implements Serializable {
		private static final long serialVersionUID = -1;
		private int app_id;

		private String content;

		private int parentid;

		private int from;

		private int mem_id;

		private long create_time;

		public void setApp_id(int app_id) {
			this.app_id = app_id;
		}

		public int getApp_id() {
			return this.app_id;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getContent() {
			return this.content;
		}

		public void setParentid(int parentid) {
			this.parentid = parentid;
		}

		public int getParentid() {
			return this.parentid;
		}

		public void setFrom(int from) {
			this.from = from;
		}

		public int getFrom() {
			return this.from;
		}

		public void setMem_id(int mem_id) {
			this.mem_id = mem_id;
		}

		public int getMem_id() {
			return this.mem_id;
		}

		public void setCreate_time(long create_time) {
			this.create_time = create_time;
		}

		public long getCreate_time() {
			return this.create_time;
		}
	}

	/**
	 * 见内容转换为map
	 * **/
	public HashMap<String, Object> getComment() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (getCode() > 199 && getCode() < 400) {
			Comment comm = getData();
			map.put("content", comm.getContent());
			map.put("from", comm.getFrom());
			map.put("mem_id", comm.getMem_id());
			map.put("create_time",System.currentTimeMillis());
			map.put("author", "");//这个需要填写
		}
		return map;
	}
}
