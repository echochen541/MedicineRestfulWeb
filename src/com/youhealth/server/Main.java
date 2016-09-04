package com.youhealth.server;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.alibaba.fastjson.JSON;
import com.youhealth.bean.Medicine;
import com.youhealth.db.DBHelper;

@Path("/main")
public class Main {
	@GET
	@Produces("text/plain")
	public String get() {
		return "You Health Team from Fudan University";
	}

	@Path("/query")
	@GET
	@Produces("text/plain")
	public String handleGet(@QueryParam("phonenumber") String phonenumber, @QueryParam("password") String password) {
		System.out.println("phonenumber = " + phonenumber + " password = " + password);
		return "Success!";
	}

	@Path("/retrieve")
	@GET
	@Produces("text/plain")
	public String retrieveMedicine(@QueryParam("name") String name) throws UnsupportedEncodingException {
		String sql = "select * from medicine where name like '%" + name + "%'";
		ResultSet ret = null;
		// System.out.println(sql);
		DBHelper db1 = new DBHelper(sql);
		List<Medicine> list = new ArrayList<Medicine>();
		try {
			ret = db1.pst.executeQuery();
			while (ret.next()) {
				String medicineName = ret.getString(1);
				String category1 = ret.getString(2);
				String category2 = ret.getString(3);
				String category3 = ret.getString(4);
				int isOtc = ret.getInt(5);
				String specification = ret.getString(6);
				String article = ret.getString(7);
				String url = ret.getString(8);
				// System.out.println(medicineName + "\t" + category1 + "\t" +
				// category2 + "\t" + category3 + "\t" + isOtc
				// + "\t" + specification + "\t" + article + "\t" + url);
				list.add(new Medicine(medicineName, category1, category2, category3, isOtc, specification, article,
						url));
			}
			ret.close();
			db1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String jsonText = JSON.toJSONString(list, true);
		return jsonText;
	}
}