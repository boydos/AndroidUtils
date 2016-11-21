package com.ds.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonModel {
	private Map<String, Object> objects = new HashMap<String, Object>();
	public JsonModel() {
		
	}
	public JsonModel(String jsonString) {
		this.fromJson(jsonString);
	}
	public void set(String key,Object value) {
		objects.put(key, value);
	}
	public void setInt(String key,int value) {
		set(key, value);
	}
	public void setString(String key,String value) {
		set(key, value);
	}
	public void setBoolean(String key,boolean value) {
		set(key, value);
	}
	public void setFloat(String key,float value) {
		set(key, value);
	}
	public void setDouble(String key,double value) {
		set(key, value);
	}
	public void setModel(String key,JsonModel model) {
		set(key, model);
	}
	public void setList(String key,List<Object> list) {
		set(key, list);
	}
	
	public Object get(String key) {
		return objects.get(key);
	}
	public String getString(String key) {
		return getString(key,null);
	}
	public String getString(String key,String def){
		Object ret = get(key);
		try{
			if(ret==null) return def;
			return ret.toString();
		}catch(Exception e) {
			
		}
		return def;
	}
	public int getInt(String key) {
		return getInt(key, 0);
	}
	public int getInt(String key,int def){
		String ret = getString(key);
		try{
			if(ret==null) return def;
			return Integer.parseInt(ret);
		}catch(Exception ne) {
			
		}
		return def;
	}
	public float getFoat(String key) {
		return getFloat(key, 0f);
	}
	public float getFloat(String key,float def){
		String ret = getString(key);
		try{
			if(ret==null) return def;
			return Float.parseFloat(ret);
		}catch(Exception ne) {
			
		}
		return def;
	}
	public double getDouble(String key) {
		return getDouble(key,0.0);
	}
	public double getDouble(String key,double def){
		String ret = getString(key);
		try{
			if(ret==null) return def;
			return Double.parseDouble(ret);
		}catch(Exception ne) {
			
		}
		return def;
	}
	public JsonModel getModel(String key) {
		Object ret = get(key);
		try{
			if(ret instanceof JsonModel) {
				return (JsonModel)ret;
			}
		}catch(Exception ne) {
			
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<Object> getListObject(String key) {
		Object ret = get(key);
		if(ret!=null && ret instanceof List<?>) {
			try {
				return (List<Object>)ret;
			}catch(Exception e) {
				
			}
		}
		return null;
	}
	public String getMatchKey(String key) {
		Set<String> keys = keySet();
		for(String name:keys) {
			if(name.equalsIgnoreCase(key)) {
				key=name;
			}
		}
        return key;
	}
	public boolean containsMatchKey(String key) {
		Set<String> keys = keySet();
		for(String name:keys) {
			if(name.equalsIgnoreCase(key)) {
				return true;
			}
		}
        return false;
	}
	public boolean containsKey(String key) {
		Set<String> keys = keySet();
		for(String name:keys) {
			if(name.equals(key)) {
				return true;
			}
		}
        return false;
	}
	public Set<String> keySet(){
		return objects.keySet();
	}
	public void remove(String key) {
		objects.remove(key);
	}
	public void changeKey(String oldKey, String newKey){
		String matchKey = getMatchKey(oldKey);
		Object value = get(matchKey);
		this.remove(matchKey);
		set(newKey, value);
	}
	public List<JsonModel> getList(String key) {
		List<Object> list = getListObject(key);
		if(list !=null){
			List<JsonModel> jsons = new ArrayList<JsonModel>();
			for(Object obj :list) {
				if(obj instanceof JsonModel) {
					jsons.add((JsonModel)obj);
				}
			}
			return jsons;
		}
		return null;
	}
	public List<String> getListString(String key) {
		List<Object> list = getListObject(key);
		if(list !=null){
			List<String> jsons = new ArrayList<String>();
			for(Object obj :list) {
				jsons.add(String.valueOf(obj));
			}
			return jsons;
		}
		return null;
	}
	public JSONObject toJsonObject() {
		Iterator<String> keys = objects.keySet().iterator();
		JSONObject jsonObj = new JSONObject();
		String key=null;
		Object value=null;
		List<Object> list=null;
		JSONArray jsonArr=null;
		while(keys.hasNext()) {
			key = keys.next();
			value = get(key);
			if(value ==null) continue;
		    if(value instanceof JsonModel) {
				jsonObj.put(key,((JsonModel)value).toJsonObject());
			} else if(value instanceof List){
				list = getListObject(key);
				jsonArr = new JSONArray();
				for(Object obj:list) {
					if(obj!=null && obj instanceof JsonModel) {
						jsonArr.add(((JsonModel)obj).toJsonObject());
					} else {
						jsonArr.add(obj);
					}
				}
			    jsonObj.put(key, jsonArr);
			} else {
				jsonObj.put(key, value);
			}
		}
		return jsonObj;
	}
	public void clear(){
		objects.clear();
	}
	public String toJson() {
		return toJsonObject().toString();
	}
	
	protected void fromJson(String jsonString) {
		JSONObject jobj = JSONObject.fromObject(jsonString);
		parseJson(this, jobj);
	}
	@SuppressWarnings("unchecked")
	private void parseJson(JsonModel model,JSONObject jsonObject) {
		try {
			JSONArray jarray=null;
			String key =null;
			Object obj =null,item=null;
			List<Object> list = null;
			JsonModel smodel=null,rmodel=null;
			if(jsonObject==null)return;
			Iterator<Object> its= jsonObject.keys();
			while(its.hasNext()) {
				key =(String)its.next();
				obj =jsonObject.get(key);
				if(obj instanceof JSONObject) {
					smodel =new JsonModel();
					model.set(key, smodel);
					parseJson(smodel, (JSONObject)obj);
				} else if(obj instanceof JSONArray) {
					jarray =(JSONArray)obj;
					list = new ArrayList<Object>();
					model.set(key, list);
					if(jarray!=null) {
						for(int i=0;i<jarray.size();i++) {
							item = jarray.get(i);
							if(item instanceof JSONObject) {
								rmodel = new JsonModel();
								parseJson(rmodel, (JSONObject)item);
								list.add(rmodel);
							}else {
								list.add(item);
							}
						}
					}
				} else {
					model.set(key, obj);
				}
			}
		}catch(Exception e) {
			
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.toJson();
	}
	public static String toJsonString(List<?> list){
	     JSONArray jarray =JSONArray.fromObject(list);
	     return jarray.toString();
	}
	public static List<Object> fromJsonString(String json) {
		JSONArray array = JSONArray.fromObject(json);
		return JsonArray2List(array);
	}
	
	private static List<Object> JsonArray2List(JSONArray jsonArray) {
		List<Object> list = new ArrayList<Object>();
		if(jsonArray ==null)return list;
		int cnt = jsonArray.size();
        for(int i=0;i<cnt;i++){
            Object obj = jsonArray.get(i);
            if(obj instanceof JSONArray){
                list.add(JsonArray2List((JSONArray)obj));
            }else{
                list.add(jsonArray.get(i));
            }

        }
		return list;
	}
}
