package swimsWeb.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
//import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import swimsEJB.model.TestManager;
import swimsEJB.model.auth.managers.GroupManager;

@Named
@RequestScoped
public class TestBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@EJB
	private GroupManager groupManager;
	@EJB
	private TestManager testManager;

	public TestBean() {
		// TODO Auto-generated constructor stub
	}
	
	public void test() {
//		List<Integer> found = groupManager.findRootGroupUserIds();
//		for (Integer integer : found) {
//			System.out.println(integer);
//		}
		try {
			testManager.test();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
