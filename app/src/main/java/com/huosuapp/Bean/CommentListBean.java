package com.huosuapp.Bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentListBean implements Serializable {
	private static final long serialVersionUID = -1;
	private int code;

	private String msg;

	private Data data;

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

	public void setData(Data data) {
		this.data = data;
	}

	public Data getData() {
		return this.data;
	}

	public static class Data implements Serializable{
		private static final long serialVersionUID = -1;
		private int count;

		@Override
		public String toString() {
			return "Data [count=" + count + ", commentlist=" + commentlist
					+ "]";
		}

		private List<Commentlist> commentlist;

		public void setCount(int count) {
			this.count = count;
		}

		public int getCount() {
			return this.count;
		}

		public void setCommentlist(List<Commentlist> commentlist) {
			this.commentlist = commentlist;
		}

		public List<Commentlist> getCommentlist() {
			return this.commentlist;
		}

	}

	public static class Commentlist implements Serializable{
		private static final long serialVersionUID = -1;
		@Override
		public String toString() {
			return "Commentlist [id=" + id + ", content=" + content
					+ ", pudate=" + pudate + ", from=" + from + ", author="
					+ author + ", mem_idauthorid=" + mem_idauthorid
					+ ", portrait=" + portrait + "]";
		}

		private int id;

		private String content;

		private String pudate;

		private int from;

		private String author;

		private int mem_idauthorid;

		private String portrait;

		public void setId(int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getContent() {
			return this.content;
		}

		public void setPudate(String pudate) {
			this.pudate = pudate;
		}

		public String getPudate() {
			return this.pudate;
		}

		public void setFrom(int from) {
			this.from = from;
		}

		public int getFrom() {
			return this.from;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getAuthor() {
			return this.author;
		}

		public void setMem_idauthorid(int mem_idauthorid) {
			this.mem_idauthorid = mem_idauthorid;
		}

		public int getMem_idauthorid() {
			return this.mem_idauthorid;
		}

		public void setPortrait(String portrait) {
			this.portrait = portrait;
		}

		public String getPortrait() {
			return this.portrait;
		}

	}

	public ArrayList<HashMap<String, Object>> getCommentList() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		if (getCode() > 199 && getCode() < 400) {
			Data data = getData();
			List<Commentlist> comments = data.getCommentlist();
			for (Commentlist bean : comments) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("content", bean.getContent());
				map.put("from", bean.getFrom());
				map.put("mem_id", bean.getMem_idauthorid());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yy-MM-dd HH:mm");
				long t = 0;
				try {
					t = sdf.parse(bean.getPudate()).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				map.put("create_time", t);
				map.put("author", bean.getAuthor());
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public String toString() {
		return "CommentListBean [code=" + code + ", msg=" + msg + ", data="
				+ data + "]";
	}
}
