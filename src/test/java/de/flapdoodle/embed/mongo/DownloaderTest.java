/**
 * Copyright (C) 2011
 *   Michael Mosmann <michael@mosmann.de>
 *   Martin Jöhren <m.joehren@googlemail.com>
 *
 * with contributions from
 * 	konstantin-ba@github,Archimedes Trajano	(trajano@github)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.embed.mongo;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import de.flapdoodle.os.Platform;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;

import de.flapdoodle.embed.process.config.store.DownloadConfig;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.distribution.Version;
import de.flapdoodle.embed.process.io.progress.ProgressListener;
import de.flapdoodle.embed.process.store.Downloader;
import de.flapdoodle.embed.process.store.UrlConnectionDownloader;

/**
 * Created with IntelliJ IDEA.
 * User: m.joehren
 * Date: 16.07.12
 * Time: 22:10
 * To change this template use File | Settings | File Templates.
 */
public class DownloaderTest {

	private static final int LISTEN_PORT = 17171;
	Server server;

	@Rule
	public TemporaryFolder tempDir = new TemporaryFolder();
	private DownloadConfig dc;
	private ProgressListener pl;

	@Before
	public void setUp() throws Exception {
		// start the jetty container
		server = new Server(LISTEN_PORT);
		File myTmpDir = tempDir.newFolder();
		File osxDir = new File(myTmpDir, "osx");
		osxDir.mkdir();
		File tempFile = new File(osxDir, "mongodb-osx-x86_64-3.1.1.tgz");
		tempFile.createNewFile();
		ResourceHandler publicDocs = new ResourceHandler();
		publicDocs.setResourceBase(myTmpDir.getCanonicalPath());

		HandlerList hl = new HandlerList();
		hl.setHandlers(new Handler[]{publicDocs});
		server.setHandler(hl);

		server.start();

		while (true) {
			if (server.isRunning()) {
				break;
			}
			Thread.sleep(100);
		}
		dc = mock(DownloadConfig.class);
		pl = mock(ProgressListener.class);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

//	//@Test
//	public void testDownload() throws Exception {
//		initRuntime();
//		Distribution d = Distribution.of(Version.of("3.1.1"), Platform.detect());
//		Downloader downloader = new UrlConnectionDownloader();
//		assertNotNull(downloader.download(dc, d));
//	}

	private void initRuntime() {
		//when(rc.getDefaultfileNaming()).thenReturn(new UUIDTempNaming());
		when(dc.getDownloadPath()).thenReturn((__) -> "http://localhost:" + LISTEN_PORT + "/");
		when(dc.getProgressListener()).thenReturn(pl);
	}

//	@Test(expected = Exception.class)
//	public void testDownloadShouldThrowExceptionForUnknownVersion() throws Exception {
//		initRuntime();
//		Distribution d = Distribution.of(Version.of("3013.1.1"), Platform.detect());
//		Downloader downloader = Downloader.platformDefault();
//		assertNotNull(downloader.download(dc, d));
//	}
}
