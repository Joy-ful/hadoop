package com.hadoop.demo1;

import com.google.gson.Gson;

//Biz 类的定义
public class Biz {
	private long id;
	private String name;
	private String add;
	
	public Biz(){}
	
	public Biz(long id, String name, String add){
		this.id = id;
		this.name = name;
		this.add = add;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}
}

//Bizs 类的定义
class Bizs {
	private Biz[] biz;
	
	public Bizs(){}
	
	public Bizs(Biz[] biz){
		this.biz = biz;
	}

	public Biz[] getBiz() {
		return biz;
	}

	public void setBiz(Biz[] biz) {
		this.biz = biz;
	}
}

//Data 类的定义
class Data {
	private int total;
	private String url;
	private Bizs bizs;
	
	public Data(){}
	
	public Data(int total, String url, Bizs bizs){
		this.total = total;
		this.url = url;
		this.bizs = bizs;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Bizs getBizs() {
		return bizs;
	}
	public void setBizs(Bizs bizs) {
		this.bizs = bizs;
	}
}

//main 方法
;
class GsonExample {
	
	public static void main(String[] args) {		
		Gson gson = new Gson();
		Data data = gson.fromJson(json, Data.class);
		Bizs details = data.getBizs();
		System.out.println("details=" + details);
	}
	
	static String json = "{\"total\":300,\"url\":\"http:wap.abc.com\",\"bizs\":	{"
		+	"\"biz\":[{\"id\":555555,\"name\":\"兰州烧饼\",\"add\":\"北京市海定区中关村\"},"
		+	"{\"id\":666666,\"name\":\"兰州拉面\",\"add\":\"北京市海定区中关村\"},"
		+	"{\"id\":888888,\"name\":\"肯德基\",\"add\":\"北京市海定区中关村\"}]}}";
}