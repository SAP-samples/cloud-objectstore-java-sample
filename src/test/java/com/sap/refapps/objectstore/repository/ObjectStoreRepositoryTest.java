package com.sap.refapps.objectstore.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.InputStream;
import java.util.List;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.sap.refapps.objectstore.model.BlobFile;
import com.sap.refapps.objectstore.util.ObjectStoreUtil;

@ContextConfiguration({ "classpath:testApplicationContext.xml" })
public class ObjectStoreRepositoryTest {

	final ObjectStoreRepository RepoObj = new ObjectStoreRepository();
	final ObjectStoreRepository spy = spy(RepoObj);
	final String bucketName = "hcp-7ee3a768-81ef-4818-98d6-55a309a4004d";
	final String fileName = "file1";
	BlobStoreContext context;

	@Before
	public void setup() throws Exception {
		context = getBlobStoreContext();
	}

	@After
	public void tearDown() throws Exception {
		BlobStore blobStore = context.getBlobStore();
		blobStore.removeBlob(bucketName, fileName);
		context.close();
	}

	@Test
	public void testUploadFile() throws Exception {
		doReturn(context).when(spy).getContext();
		String message = uploadFileForTesting();
		String expectedMessage = fileName + ObjectStoreUtil.UPLOAD_SUCCESSFUL;
		assertTrue(message.contains(expectedMessage));
	}

	@Test
	public void testListFile() throws Exception {
		doReturn(context).when(spy).getContext();
		uploadFileForTesting();
		List<BlobFile> files = spy.listFiles(bucketName);
		assertTrue(files.size() > 0);
	}

	@Test
	public void testDownloadFile() throws Exception {
		doReturn(context).when(spy).getContext();
		uploadFileForTesting();
		InputStream is = spy.downloadFile(bucketName, fileName);
		String fileContent = is.toString();
		assertTrue(fileContent.length() > 0);
	}

	@Test
	public void testDeleteFile() throws Exception {
		doReturn(context).when(spy).getContext();
		uploadFileForTesting();
		boolean deleteStatus = spy.deleteFile(bucketName, fileName);
		if (deleteStatus) {
			doReturn(context).when(spy).getContext();
			assertFalse(spy.isBlobExist(bucketName, fileName));
		}

	}

	private BlobStoreContext getBlobStoreContext() {
		return ContextBuilder.newBuilder("aws-s3")
				.credentials("AKIAWEVHTUEPK57EY4QF", "KmC58xTrxeVOO6D50orXtlgfT4N771cSuiEQkLXu")
				.buildView(BlobStoreContext.class);
	}
	
	private String uploadFileForTesting(){
		byte data[] = "Bytes To Test".getBytes();
		final String contentType = "text/plain";
		return spy.uploadFile(bucketName, data, fileName, contentType);
		
	}

}
