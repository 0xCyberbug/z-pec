package com.zimbra.qa.selenium.projects.octopus.tests.myfiles.files;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.octopus.ui.DialogMove;
import com.zimbra.qa.selenium.projects.octopus.ui.PageHistory;
import com.zimbra.qa.selenium.projects.octopus.ui.PageHistory.GetText;
import com.zimbra.qa.selenium.projects.octopus.core.OctopusCommonTest;
import com.zimbra.qa.selenium.projects.octopus.ui.PageMyFiles;

public class MoveFile extends OctopusCommonTest {

	private boolean _folderIsCreated = false;
	private String _folderName = null;
	private boolean _fileAttached = false;
	private String _fileId = null;

	public MoveFile() {
		logger.info("New " + MoveFile.class.getCanonicalName());

		// test starts at the My Files tab
		super.startingPage = app.zPageMyFiles;
		super.startingAccountPreferences = null;
	}

	@BeforeMethod(groups = { "always" })
	public void testReset() {
		_folderName = null;
		_folderIsCreated = false;
		_fileId = null;
		_fileAttached = false;
	}

	@Test(description = "Move file using soap - verify file is moved", groups = { "functional" })
	public void MoveFile_01() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseRootFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create sub-folder
		String subFolderName = "subFolder"
				+ ZimbraSeleniumProperties.getUniqueString();

		// Create a subfolder to move the file into i.e. My Files/subfolder
		account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+ "<folder name='" + subFolderName + "' l='"
				+ briefcaseRootFolder.getId() + "' view='document'/>"
				+ "</CreateFolderRequest>");

		// Verify the sub-folder exists on the server
		FolderItem subFolderItem = FolderItem.importFromSOAP(account,
				subFolderName);
		ZAssert.assertNotNull(subFolderItem,
				"Verify the subfolder is available");

		String subFolderId = subFolderItem.getId();
		_folderName = subFolderName;
		_folderIsCreated = true;

		// Create file item
		String filePath = ZimbraSeleniumProperties.getBaseDirectory()
				+ "/data/public/other/testtextfile.txt";

		FileItem file = new FileItem(filePath);

		String fileName = file.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to the root folder through SOAP
		account.soapSend(

		"<SaveDocumentRequest xmlns='urn:zimbraMail'>" +

		"<doc l='" + briefcaseRootFolder.getId() + "'>" +

		"<upload id='" + attachmentId + "'/>" +

		"</doc>" +

		"</SaveDocumentRequest>");

		_fileAttached = true;
		_fileId = account.soapSelectValue(
				"//mail:SaveDocumentResponse//mail:doc", "id");

		// verify the file is uploaded
		ZAssert.assertNotNull(_fileId, "Verify file is uploaded");

		// move file using SOAP
		app.zPageOctopus.moveItemUsingSOAP(_fileId, subFolderId, account);

		// click on sub-folder
		app.zPageMyFiles.zListItem(Action.A_LEFTCLICK, subFolderName);

		// Verify the file is now in the destination folder
		ZAssert.assertTrue(app.zPageOctopus.zIsItemInCurentListView(fileName),
				"Verify the file was moved to the destination folder");				
	}

	@Test(description = "Move file using context menu - verify file is moved", groups = { "sanity" })
	public void MoveFile_02() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseRootFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create sub-folder
		String subFolderName = "subFolder"
				+ ZimbraSeleniumProperties.getUniqueString();

		// Create a subfolder to move the file into i.e. My Files/subfolder
		account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+ "<folder name='" + subFolderName + "' l='"
				+ briefcaseRootFolder.getId() + "' view='document'/>"
				+ "</CreateFolderRequest>");

		// Verify the sub-folder exists on the server
		FolderItem subFolderItem = FolderItem.importFromSOAP(account,
				subFolderName);
		ZAssert.assertNotNull(subFolderItem,
				"Verify the subfolder is available");

		_folderName = subFolderName;
		_folderIsCreated = true;

		// Create file item
		String filePath = ZimbraSeleniumProperties.getBaseDirectory()
				+ "/data/public/other/testtextfile.txt";

		FileItem file = new FileItem(filePath);

		String fileName = file.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to the root folder through SOAP
		account.soapSend(

		"<SaveDocumentRequest xmlns='urn:zimbraMail'>" +

		"<doc l='" + briefcaseRootFolder.getId() + "'>" +

		"<upload id='" + attachmentId + "'/>" +

		"</doc>" +

		"</SaveDocumentRequest>");

		_fileAttached = true;
		_fileId = account.soapSelectValue(
				"//mail:SaveDocumentResponse//mail:doc", "id");

		// verify the file is uploaded
		ZAssert.assertNotNull(_fileId, "Verify file is uploaded");

		// move file using right click context menu
		DialogMove chooseFolder = (DialogMove) app.zPageMyFiles
				.zToolbarPressPulldown(Button.B_MY_FILES_LIST_ITEM,
						Button.O_MOVE, fileName);

		// Double click to choose folder
		chooseFolder.zDoubleClickTreeFolder(subFolderName);

		// Verify the moved file disappears from My Files tab
		ZAssert.assertTrue(app.zPageMyFiles.zWaitForElementDeleted(
				PageMyFiles.Locators.zMyFilesListView.locator + ":contains("
						+ fileName + ")", "5000"),
				"Verify the moved file disappears from My Files tab");

		// click on sub-folder
		app.zPageMyFiles.zListItem(Action.A_LEFTCLICK, subFolderName);

		// Verify the file is now in the destination folder
		ZAssert.assertTrue(app.zPageMyFiles.zWaitForElementPresent(
				PageMyFiles.Locators.zMyFilesListView.locator + ":contains("
						+ fileName + ")", "3000"),
				"Verify the file was moved to the destination folder");
	}
	
	@Test(description = "Ensure folder link in history opens that folder", groups = { "smoke" })
	public void VerifyFolderLinkFromHistory() throws HarnessException 
	{
		//Create folder and Move file.
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseRootFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create sub-folder
		String subFolderName = "subFolder"+ ZimbraSeleniumProperties.getUniqueString();
		String rootFolder = "My Files";

		// Create a sub folder to move the file into i.e. My Files/subfolder
		account.soapSend(
						"<CreateFolderRequest xmlns='urn:zimbraMail'>"
								+ "<folder name='" + subFolderName + "' l='"
								+ briefcaseRootFolder.getId() + "' view='document'/>"
					  + "</CreateFolderRequest>"
						 );

		// Create file item
		String fileName = TEXT_FILE;
		uploadFileViaSoap(app.zGetActiveAccount(), fileName);

		// verify the file is uploaded
		ZAssert.assertNotNull(fileName, "Verify file is uploaded");

		// move file using right click context menu
		DialogMove chooseFolder = (DialogMove) app.zPageMyFiles.zToolbarPressPulldown(Button.B_MY_FILES_LIST_ITEM,
				Button.O_MOVE, fileName);

		// Double click to choose folder
		chooseFolder.zDoubleClickTreeFolder(subFolderName);

		// Verify the moved file disappears from My Files tab
		ZAssert.assertTrue(app.zPageMyFiles.zWaitForElementDeleted(
				PageMyFiles.Locators.zMyFilesListView.locator + ":contains("
						+ fileName + ")", "5000"),
				"Verify the moved file disappears from My Files tab");

		// Click on History tab
		app.zPageOctopus.zToolbarPressButton(Button.B_TAB_HISTORY);

		String requiredHistory = "You moved file "+ fileName + " from folder " + rootFolder + " to folder " + subFolderName +".";

		//Assert if found history matches with upload file history
		ZAssert.assertEquals(GetText.move(fileName,rootFolder,subFolderName), app.zPageHistory.isTextPresentInGlobalHistory(requiredHistory).getHistoryText(), "Verify if required history matches with found history");

		app.zPageHistory.sClickAt(PageHistory.Locators.zHistoryFolderLink.locator, "0,0");

		// Verify the file is now in the destination folder
		ZAssert.assertTrue(app.zPageMyFiles.zWaitForElementPresent(
				PageMyFiles.Locators.zMyFilesListView.locator + ":contains("
						+ fileName + ")", "3000"),"Verify the file is avilable");
	}


	@AfterMethod(groups = { "always" })
	public void testCleanup() {
		if (_fileAttached && _fileId != null) {
			try {
				// Delete it from Server
				app.zPageOctopus.deleteItemUsingSOAP(_fileId,
						app.zGetActiveAccount());
			} catch (Exception e) {
				logger.info("Failed while deleting the file", e);
			} finally {
				_fileId = null;
				_fileAttached = false;
			}
		}
		if (_folderIsCreated) {
			try {
				// Delete it from Server
				FolderItem
						.deleteUsingSOAP(app.zGetActiveAccount(), _folderName);
			} catch (Exception e) {
				logger.info("Failed while removing the folder.", e);
			} finally {
				_folderName = null;
				_folderIsCreated = false;
			}
		}
		try {
			// Refresh view 
			//ZimbraAccount account = app.zGetActiveAccount();
			//FolderItem item = FolderItem.importFromSOAP(account,SystemFolder.Briefcase);
			//account.soapSend("<GetFolderRequest xmlns='urn:zimbraMail'><folder l='1' recursive='0'/>" + "</GetFolderRequest>");
			//account.soapSend("<GetFolderRequest xmlns='urn:zimbraMail' requestId='folders' depth='1' tr='true' view='document'><folder l='" + item.getId() + "'/></GetFolderRequest>");
			//account.soapSend("<GetActivityStreamRequest xmlns='urn:zimbraMail' id='16'/>");
			//app.zGetActiveAccount().accountIsDirty = true;
			//app.zPageOctopus.sRefresh();
												
			// Empty trash
			app.zPageTrash.emptyTrashUsingSOAP(app.zGetActiveAccount());
			
			app.zPageOctopus.zLogout();
		} catch (Exception e) {
			logger.info("Failed while emptying Trash", e);
		}
	}
}
