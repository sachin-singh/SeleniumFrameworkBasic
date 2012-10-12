package com.clogic.SeleniumFramework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.*;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;



/**
 * The Class Test1.
 *
 * 
 */
public class ClassTest extends BasicSetUp{
	
	LogInPage loginPage;
	CCHomePage callCenter ;
	AgentPage agentPage;
	RegisterPage registerPage;
	AdminHomePage adminHomePage;
	ProjectPage projectPage;
	CRMPage crmPage;
	AdvancedPage advancePage;
	DNCPage dncPage;
	Properties prop ; 
	
	String adminEmail ;
	String adminPassword ;
	String baseUrl ; 
	String callCenterEmail ;
	String callCenterPassword ;
	
	@Test(description="Read Config")
	public ClassTest(){
		prop = new Properties();
		try{
			prop.load(new FileInputStream("config.properties"));
			baseUrl= prop.getProperty("baseurl");
			callCenterEmail= prop.getProperty("cc_username");
			callCenterPassword= prop.getProperty("cc_password");
			adminEmail= prop.getProperty("admin_username");
			adminPassword=prop.getProperty("admin_password");
			writeText("Reading Config");
			System.out.println("Reading Config");
			
		}
		catch(Exception e){
			writeText("Error: Couldn't Read Config");
			System.out.println("Error: Couldn't Read Config");
		}
	}
	
	
	@Test(description="Log in as Admin")
	public void loginAdmin() throws  Exception{		
		loginPage = new LogInPage(driver, baseUrl);
		sleep(5);
		adminHomePage= loginPage.loginAsAdmin(adminEmail, adminPassword);
		sleep(5);
	}
	
	@Test(description="Log in")
	public void login() throws Exception{
		loginPage = new LogInPage(driver, baseUrl);
		sleep(5);
		callCenter= loginPage.login(callCenterEmail, callCenterPassword);
		sleep(5);
	}
	
	
	@Test(description="Test Ride")
	public void newTest() throws Exception{
		loginAdmin();
		callCenter = adminHomePage.enterCallCenter(callCenterEmail);
		sleep(2);
		crmPage = callCenter.gotoCRMPage();
		sleep(2);
		for(int i=1;i<=21;i++){
			sleep(2);
			crmPage.gotoAssignLeads();
			sleep(2);
			crmPage.assignAllLeads("CodeProject"+i);
		}
		
	}
	
	

	
	 
	
	
	
	@Test(description= "Complete Portal Check")
	public void portalTest() throws Exception{
		
	}
	
	
	
	@Test(description ="NewAccountCreation" )
	public void newAccountCreationTest() throws Exception{
		
		String email =prop.getProperty("newcc_email")	;
		String fullName =prop.getProperty("newcc_name");
		String password=prop.getProperty("newcc_password");
		String title=prop.getProperty("newcc_title");
		String organization=prop.getProperty("newcc_org");
		String address=prop.getProperty("newcc_address");
		String city=prop.getProperty("newcc_city");
		String state=prop.getProperty("newcc_state");
		String postalCode=prop.getProperty("newcc_postal");
		String country=prop.getProperty("newcc_country");
		String phone=prop.getProperty("newcc_phone");
		String recommendation =prop.getProperty("newcc_recommedation");
		String serverId = prop.getProperty("serverId");
		int cc_type= new Integer((prop.getProperty("cc_type")));
		
		
		loginPage = new LogInPage(driver ,baseUrl );
		 
		registerPage  = loginPage.gotoRegistrationPage();
		registerPage.createAccount(email, fullName, password, title, organization, address, city, state, postalCode, country, phone, recommendation);
		Thread.sleep(5000);
//		verifyTrue((driver.getCurrentUrl().equals(baseUrl+"ls/static/registration_sucessful.html")) , "Error:During call center creation");
		
		 
		adminHomePage = loginPage.loginAsAdmin(adminEmail, adminPassword);
		adminHomePage.activateAccount(email,password,cc_type, serverId);
		adminHomePage.signOut(); 
		
		
		
	}
	
	
	@Test(description="Create Call Center")
	public void createCallCenterTest() throws Exception { 
		String callCenterName=prop.getProperty("newcc_ccname");
		String callCenterTimeZone=prop.getProperty("newcc_timezone");
		String callCenterLanguage= prop.getProperty("newcc_language");
		String callCenterCurrency=	prop.getProperty("newcc_currency");
		
		login();
		callCenter.createCallCenter(callCenterName, callCenterTimeZone, callCenterLanguage, callCenterCurrency);
		Thread.sleep(5000);
		verifyTrue((driver.getCurrentUrl().equals(baseUrl+"/ms/static/setup.html")), "Error: CallCenter during creation"); 
		callCenter.signOut();
		
		
	}
	
	@Test(description="New Account Creation by Admin")
	public void newAccountCreationByAdminTest() throws Exception{
		String email =prop.getProperty("newcc_email");
		String fullName =prop.getProperty("newcc_name");
		String password=prop.getProperty("newcc_password");
		String role=prop.getProperty("newcc_role");
		int setStatus= Integer.parseInt(prop.getProperty("newcc_status"));
		String title=prop.getProperty("newcc_title");
		String organization=prop.getProperty("newcc_org");
		String address=prop.getProperty("newcc_address");
		String city=prop.getProperty("newcc_city");
		String state=prop.getProperty("newcc_state");
		String postalCode=prop.getProperty("newcc_postal");
		String country=prop.getProperty("newcc_country");
		String phone=prop.getProperty("newcc_phone");
		String recommendation =prop.getProperty("newcc_recommedation");
		
		String callCenterName=prop.getProperty("newcc_ccname");
		String callCenterTimeZone=prop.getProperty("newcc_timezone");
		String callCenterLanguage= prop.getProperty("newcc_language");
		String callCenterCurrency=	prop.getProperty("newcc_currency");
			
			
		loginPage = new LogInPage(driver, baseUrl);
		adminHomePage = loginPage.loginAsAdmin("admin@3clogic.com", "webastra");
		adminHomePage.createAccountByAdmin(email, fullName, password, role, setStatus, title, organization, address, city, state, postalCode, country, phone, recommendation);
		adminHomePage.signOut();
		
		
		callCenter = loginPage.login(email, password);
		callCenter.createCallCenter(callCenterName, callCenterTimeZone, callCenterLanguage, callCenterCurrency);
		verifyTrue((driver.getCurrentUrl().equals(baseUrl+"/ms/static/setup.html")), "Error: CallCenter during creation"); 
		callCenter.signOut();
	}
	
	
	@Test(description="Update Call Center Profile")
	public void updateCallCenterProfileTest() throws Exception{
		String oldPassword=prop.getProperty("updatecc_oldPassword");
		String newPassword=prop.getProperty("updatecc_newPassword");
		String confirmedNewPassword=prop.getProperty("updatecc_confirmNewPassword");
		String emailAddress=prop.getProperty("updatecc_emailAddress");
		String fullName=prop.getProperty("updatecc_name");
		String address=prop.getProperty("updatecc_address");
		String city=prop.getProperty("updatecc_city");
		String state=prop.getProperty("updatecc_state");
		String country=prop.getProperty("updatecc_country");
		String zipCode=prop.getProperty("updatecc_postal");
		String phone=prop.getProperty("updatecc_phone");
		String timeZone=prop.getProperty("updatecc_timezone");
		String language=prop.getProperty("updatecc_language");
		String currency=prop.getProperty("updatecc_currency");
		
		login();	
		callCenter.gotoProfile();
		callCenter.updateProfile(oldPassword, newPassword, confirmedNewPassword, emailAddress, fullName, address, city, state, country, zipCode, phone, timeZone, language, currency);
		verifyTrue(selenium.isTextPresent("Your Email address has been successfully updated."), "Error: While updating profile");
		callCenter.signOut();
	}
	
	
	@Test(description="New Project Creation")
	public void createProjectTest() throws Exception{
		String newProjectName=prop.getProperty("newProj_name");
		
		login();
		projectPage = callCenter.gotoProjectPage();
		sleep(5); 
		projectPage.createOutboundProject(newProjectName);
		sleep(2); 
		projectPage.signOut();
	}
	
	
	@Test(description="Delete Existing Project")
	public void deleteProjectTest() throws Exception{
		
		callCenter= adminHomePage.enterCallCenter("vsharma@3clogic.com");
		sleep(5);
		projectPage = callCenter.gotoProjectPage();
		sleep(5);
		for(int i=0;i<50;i++){
			projectPage.deleteProject("Testing Project"+i);
			sleep(2); 
			} 	 
	}
	
	
	@Test(description="Create Agent")
	public void createAgentTest() throws Exception{  
		String newAgentName=prop.getProperty("newAgent_name");
		String newAgentUserName=prop.getProperty("newAgent_username");
		String newAgentPassword=prop.getProperty("newAgent_password");
		String newAgentEmail=prop.getProperty("newAgent_email");
		 
		
		
		login();
		agentPage = callCenter.gotoAgentPage(); 
		agentPage.addAgent(newAgentName, newAgentUserName, newAgentPassword, newAgentEmail, false); 
		agentPage.signOut();
	}
	
	@Test(description="assign Agent")
	public void assignAgentTest() throws Exception{
		String projectName= prop.getProperty("assignAgent_in_Project");
		
		login();
		projectPage= callCenter.gotoProjectPage();
		projectPage.gotoProject(projectName);
		projectPage.assignAllAvailableAgent(); 
		projectPage.signOut();
	}
	
	@Test(description="Buy Package")
	public void buyPackageTest() throws Exception{
		String packageID=prop.getProperty("packageId");
		
		loginAdmin();
		adminHomePage.enterCallCenter(callCenterEmail);
		callCenter = adminHomePage.gotoCCHomePage();
		callCenter.gotoBuyPackages();
		callCenter.addPackages(By.id(packageID), 2	);
		callCenter.signOut();

	}
	
	@Test(description="Import leads")
	public void importLeadsTest() throws Exception{
		String path = prop.getProperty("leadList_path");
		login();
		crmPage= callCenter.gotoCRMPage();
		sleep(5);
		crmPage.gotoImportLeads();
		sleep(5);
		
		crmPage.importLeads(path);
		crmPage.signOut();
	}
	
	@Test(description= "assign leadds")
	public void assignLeadsTest() throws Exception{
		String projectName=prop.getProperty("assignlead_in_project");
		
		login();
		crmPage= callCenter.gotoCRMPage();
		crmPage.assignAllLeads(projectName);
		crmPage.signOut();
	}
	
	
	@Test(description ="Assign Package to Agent")
	public void assignPackageTest() throws Exception{
		String agentName = prop.getProperty("assignPackage_to_Agent");
		
		login();
		agentPage=callCenter.gotoAgentPage();
		agentPage.gotoAgent(agentName);
		agentPage.gotoAgentAssignPackages();
		agentPage.assignPackage();
		agentPage.signOut();
	}
	
	@Test(description="AssignAllAgent")
	public void assignAllAgentTest() throws Exception{
		
		callCenter = adminHomePage.enterCallCenter("vsharma@3clogic.com");
		sleep(5);
		projectPage = callCenter.gotoProjectPage();
		
		
		for(int i=2; i<=250;i++){
			projectPage.gotoProject("Dashboard"+i);
			while(isElementPresent(By.xpath("//input[@value='Bulk Assign']"))){
				projectPage.assignAllAvailableAgent();
				sleep(2);
				}		
			try {
				Assert.assertTrue(isElementPresent(By.cssSelector("td.blueBox")));
				writeText("All available agents assigned in "+"Dashboard"+i+" .... @"+getDate());
				System.out.println("All available agents assigned in "+"Dashboard"+i+" .... @"+getDate());
			} catch (Error e) {
				verificationErrors.append(e.toString());
				writeText("Error: While assigning agents in "+"Dashboard"+i+" .... @"+getDate());
				System.out.println("Error: While assigning agents in "+"Dashboard"+i+" .... @"+getDate());
			}
		
		
		}
		
	}
	
	
	 
	
	
	public void addpackage() throws Exception{
		callCenter = adminHomePage.enterCallCenter("vsharma@3clogic.com");
		sleep(2);
		agentPage= callCenter.gotoAgentPage();
		agentPage.gotoAgent("DashboardAgent1");	
		
	}
	
	@Test(description="Change Dial Plan")
	public void testDialPlan() throws Exception {
		callCenter = adminHomePage.enterCallCenter("vsharma@3clogic.com");
		sleep(2);
		projectPage = callCenter.gotoProjectPage();
		sleep(1);
		for(int i=1; i<=250; i++){
			projectPage.gotoProject("Dashboard"+i);
			projectPage.changeDialPlan();
		}
		
	}
	
	 
	
	
	@Test(description="Basic Sanity Test")
	public void verifyRoles() throws Exception{
		callCenter = adminHomePage.enterCallCenter("vsharma@3clogic.com");
		advancePage=callCenter.gotoAdvancePage();
		advancePage.gotoRoles();
//		advancePage.verifyAgentPortalRoles("roles.txt");
//		advancePage.showSelectedRoles();
		Thread.sleep(5000);
//		isTextPresent("project");
		
		
	}
	
	@Test(description="Lead Search Test")
	public void searchLeadTest() throws Exception{
		String firstName = prop.getProperty("search_lead_name");
		login();
		crmPage= callCenter.gotoCRMPage();
		crmPage.searchLeadByName(firstName);
		sleep(5);
		verifyTextpresent(By.cssSelector("td[title=\"Click to see/update lead details\"]"), firstName);
		crmPage.signOut();
	}
	 
	
	
	
	
	//==================================================================================================//
	
	@Test(groups = { "NewAccountCreation" })
	public void registerFormTests() throws Exception{
		loginPage = new LogInPage(driver, baseUrl);
		loginPage.gotoRegistrationPage();
		
		verifyElementPresent(By.cssSelector("img[alt=\"reCAPTCHA challenge image\"]"), "Captcha Image");
		
	}

	
	
	@Test(groups = { "DNCTabTest" })
	public void dncTabTest() throws Exception{
		login();
		dncPage = callCenter.gotoDNCPage();
		dncPage.gotoDNCLists();
		isTextPresent(prop.getProperty("nodnclist_message"));
		dncPage.addNewDNCList(prop.getProperty("newdnclistname"));
		isTextPresent(prop.getProperty("newdnclistname"));
		dncPage.deleteDNCList();
		
	}
	
	
	
	
}
