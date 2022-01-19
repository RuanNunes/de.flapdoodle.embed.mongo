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
package de.flapdoodle.embed.mongo.packageresolver;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.TestUtils;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.store.DistributionPackage;
import de.flapdoodle.os.CommonArchitecture;
import de.flapdoodle.os.OS;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlatformPackageResolverTest {

  private final PlatformPackageResolver testee = new PlatformPackageResolver(Command.Mongo);

  @Test
  public void matchKnownVersions() {
    assertThatDistributionPackageFor(Version.V4_2_13, OS.Windows, CommonArchitecture.X86_64)
            .matchesPath("/win32/mongodb-win32-x86_64-2012plus-4.2.13.zip");

    assertThatDistributionPackageFor(Version.V4_0_12, OS.Windows, CommonArchitecture.X86_64)
        .matchesPath("/win32/mongodb-win32-x86_64-2008plus-ssl-4.0.12.zip");

    assertThatDistributionPackageFor(Version.V4_0_12, OS.Linux, CommonArchitecture.X86_64)
            .matchesPath("/linux/mongodb-linux-x86_64-4.0.12.tgz");

    assertThatDistributionPackageFor(Version.V4_0_12, OS.OS_X, CommonArchitecture.X86_64)
            .matchesPath("/osx/mongodb-osx-ssl-x86_64-4.0.12.tgz");

    assertThatDistributionPackageFor(Version.V3_4_5, OS.Solaris, CommonArchitecture.X86_64)
        .matchesPath("/sunos5/mongodb-sunos5-x86_64-3.4.5.tgz");
  }

  @Test
  public void explain() {
    String explained = testee.explain();
    System.out.println(explained);
  }

  private WithDistributionPackage assertThatDistributionPackageFor(Version version, OS os, CommonArchitecture architecture) {
    return new WithDistributionPackage(testee.packageFor(TestUtils.distributionOf(version, os, architecture)));
  }

  private class WithDistributionPackage {
    private final DistributionPackage result;

    public WithDistributionPackage(DistributionPackage result) {
      this.result = result;
    }

    public void matchesPath(String path) {
      assertThat(result.archivePath()).isEqualTo(path);
    }
  }
}