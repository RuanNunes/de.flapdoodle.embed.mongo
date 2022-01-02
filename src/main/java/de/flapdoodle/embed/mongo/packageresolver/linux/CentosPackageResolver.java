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
package de.flapdoodle.embed.mongo.packageresolver.linux;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.packageresolver.*;
import de.flapdoodle.embed.process.config.store.DistributionPackage;
import de.flapdoodle.embed.process.config.store.FileSet;
import de.flapdoodle.embed.process.config.store.FileType;
import de.flapdoodle.embed.process.config.store.ImmutableFileSet;
import de.flapdoodle.embed.process.distribution.ArchiveType;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.os.BitSize;
import de.flapdoodle.os.CPUType;
import de.flapdoodle.os.OS;
import de.flapdoodle.os.linux.CentosVersion;
import de.flapdoodle.os.linux.UbuntuVersion;

import java.util.Optional;

public class CentosPackageResolver implements PackageFinder {

  private final Command command;
  private final ImmutablePlatformMatchRules rules;

  public CentosPackageResolver(Command command) {
    this.command = command;
    this.rules = rules(command);
  }

  @Override
  public Optional<DistributionPackage> packageFor(Distribution distribution) {
    return rules.packageFor(distribution);
  }

  /*
  -----------------------------------

     */
  private static ImmutablePlatformMatchRules rules(Command command) {
    ImmutableFileSet fileSet = FileSet.builder().addEntry(FileType.Executable, command.commandName()).build();

    /*
			RedHat / CentOS 6.2+ x64
			--
			5.0.2 - 5.0.0, 4.2.4 - 4.2.4, 3.4.8 - 3.4.8, 2.6.12 - 2.6.0
			https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel62-{}.tgz
			4.4.9 - 4.4.0, 4.2.16 - 4.2.5, 4.2.3 - 4.2.0, 4.0.26 - 4.0.0, 3.6.22 - 3.6.0, 3.4.23 - 3.4.9, 3.4.7 - 3.4.0, 3.2.21 - 3.2.0, 3.0.14 - 3.0.0
    */
		PlatformMatchRule centos6 = PlatformMatchRule.builder()
			.match(DistributionMatch.any(
						VersionRange.of("4.4.0", "4.4.9"),
						VersionRange.of("4.2.5", "4.2.16"),
						VersionRange.of("4.2.0", "4.2.3"),
						VersionRange.of("4.0.0", "4.0.26"),
						VersionRange.of("3.6.0", "3.6.22"),
						VersionRange.of("3.4.9", "3.4.23"),
						VersionRange.of("3.4.0", "3.4.7"),
						VersionRange.of("3.2.0", "3.2.21"),
						VersionRange.of("3.0.0", "3.0.14")
					)
					.andThen(PlatformMatch
						.withOs(OS.Linux)
						.withBitSize(BitSize.B64)
						.withCpuType(CPUType.X86)
						.withVersion(
							CentosVersion.CentOS_6
						)
					)
			)
			.finder(UrlTemplatePackageResolver.builder()
				.fileSet(fileSet)
				.archiveType(ArchiveType.TGZ)
				.urlTemplate("/linux/mongodb-linux-x86_64-rhel62-{version}.tgz")
				.build())
			.build();

		PlatformMatchRule tools_centos6 = PlatformMatchRule.builder()
			.match(DistributionMatch.any(
						VersionRange.of("4.4.0", "4.4.9"),
						VersionRange.of("4.2.5", "4.2.16"),
						VersionRange.of("4.2.0", "4.2.3"),
						VersionRange.of("4.0.0", "4.0.26"),
						VersionRange.of("3.6.0", "3.6.22"),
						VersionRange.of("3.4.9", "3.4.23"),
						VersionRange.of("3.4.0", "3.4.7"),
						VersionRange.of("3.2.0", "3.2.21"),
						VersionRange.of("3.0.0", "3.0.14")
					)
					.andThen(PlatformMatch
						.withOs(OS.Linux)
						.withBitSize(BitSize.B64)
						.withCpuType(CPUType.X86)
						.withVersion(
							CentosVersion.CentOS_6
						)
					)
			)
			.finder(UrlTemplatePackageResolver.builder()
				.fileSet(fileSet)
				.archiveType(ArchiveType.TGZ)
				.urlTemplate("/tools/db/mongodb-database-tools-rhel62-x86_64-{tools.version}.tgz")
				.build())
			.build();


    /*
			RedHat / CentOS 7.0 x64
			--
			4.2.4 - 4.2.4, 3.4.8 - 3.4.8, 2.6.12 - 2.6.0
			https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-{}.tgz
			5.0.2 - 5.0.0, 4.4.9 - 4.4.0, 4.2.16 - 4.2.5, 4.2.3 - 4.2.0, 4.0.26 - 4.0.0, 3.6.22 - 3.6.0, 3.4.23 - 3.4.9, 3.4.7 - 3.4.0, 3.2.21 - 3.2.0, 3.0.14 - 3.0.0
    */
		PlatformMatchRule centos7 = PlatformMatchRule.builder()
			.match(DistributionMatch.any(
						VersionRange.of("5.0.0", "5.0.2"),
						VersionRange.of("4.4.0", "4.4.9"),
						VersionRange.of("4.2.5", "4.2.16"),
						VersionRange.of("4.2.0", "4.2.3"),
						VersionRange.of("4.0.0", "4.0.26"),
						VersionRange.of("3.6.0", "3.6.22"),
						VersionRange.of("3.4.9", "3.4.23"),
						VersionRange.of("3.4.0", "3.4.7"),
						VersionRange.of("3.2.0", "3.2.21"),
						VersionRange.of("3.0.0", "3.0.14")
					)
					.andThen(PlatformMatch
						.withOs(OS.Linux)
						.withBitSize(BitSize.B64)
						.withCpuType(CPUType.X86)
						.withVersion(
							CentosVersion.CentOS_7
						)
					)
			)
			.finder(UrlTemplatePackageResolver.builder()
				.fileSet(fileSet)
				.archiveType(ArchiveType.TGZ)
				.urlTemplate("/linux/mongodb-linux-x86_64-rhel70-{version}.tgz")
				.build())
			.build();

		PlatformMatchRule tools_centos7 = PlatformMatchRule.builder()
			.match(DistributionMatch.any(
						VersionRange.of("5.0.0", "5.0.2"),
						VersionRange.of("4.4.0", "4.4.9"),
						VersionRange.of("4.2.5", "4.2.16"),
						VersionRange.of("4.2.0", "4.2.3"),
						VersionRange.of("4.0.0", "4.0.26"),
						VersionRange.of("3.6.0", "3.6.22"),
						VersionRange.of("3.4.9", "3.4.23"),
						VersionRange.of("3.4.0", "3.4.7"),
						VersionRange.of("3.2.0", "3.2.21"),
						VersionRange.of("3.0.0", "3.0.14")
					)
					.andThen(PlatformMatch
						.withOs(OS.Linux)
						.withBitSize(BitSize.B64)
						.withCpuType(CPUType.X86)
						.withVersion(
							CentosVersion.CentOS_7
						)
					)
			)
			.finder(UrlTemplatePackageResolver.builder()
				.fileSet(fileSet)
				.archiveType(ArchiveType.TGZ)
				.urlTemplate("/tools/db/mongodb-database-tools-rhel70-x86_64-{tools.version}.tgz")
				.build())
			.build();


    /*
			RedHat / CentOS 8.0 x64
			--
			4.2.4 - 4.2.4, 4.2.0 - 4.2.0, 4.0.13 - 4.0.0, 3.6.16 - 3.6.0, 3.4.23 - 3.4.0, 3.2.21 - 3.2.0, 3.0.14 - 3.0.0, 2.6.12 - 2.6.0
			https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel80-{}.tgz
			5.0.2 - 5.0.0, 4.4.9 - 4.4.0, 4.2.16 - 4.2.5, 4.2.3 - 4.2.1, 4.0.26 - 4.0.14, 3.6.22 - 3.6.17
    */
		PlatformMatchRule centos8 = PlatformMatchRule.builder()
			.match(DistributionMatch.any(
						VersionRange.of("5.0.0", "5.0.2"),
						VersionRange.of("4.4.0", "4.4.9"),
						VersionRange.of("4.2.5", "4.2.16"),
						VersionRange.of("4.2.1", "4.2.3"),
						VersionRange.of("4.0.14", "4.0.26"),
						VersionRange.of("3.6.17", "3.6.22")
					)
					.andThen(PlatformMatch
						.withOs(OS.Linux)
						.withBitSize(BitSize.B64)
						.withCpuType(CPUType.X86)
						.withVersion(
							CentosVersion.CentOS_8
						)
					)
			)
			.finder(UrlTemplatePackageResolver.builder()
				.fileSet(fileSet)
				.archiveType(ArchiveType.TGZ)
				.urlTemplate("/linux/mongodb-linux-x86_64-rhel80-{version}.tgz")
				.build())
			.build();

		PlatformMatchRule tools_centos8 = PlatformMatchRule.builder()
			.match(DistributionMatch.any(
						VersionRange.of("5.0.0", "5.0.2"),
						VersionRange.of("4.4.0", "4.4.9"),
						VersionRange.of("4.2.5", "4.2.16"),
						VersionRange.of("4.2.1", "4.2.3"),
						VersionRange.of("4.0.14", "4.0.26"),
						VersionRange.of("3.6.17", "3.6.22")
					)
					.andThen(PlatformMatch
						.withOs(OS.Linux)
						.withBitSize(BitSize.B64)
						.withCpuType(CPUType.X86)
						.withVersion(
							CentosVersion.CentOS_8
						)
					)
			)
			.finder(UrlTemplatePackageResolver.builder()
				.fileSet(fileSet)
				.archiveType(ArchiveType.TGZ)
				.urlTemplate("/tools/db/mongodb-database-tools-rhel80-x86_64-{tools.version}.tgz")
				.build())
			.build();


    /*
			RedHat / CentOS 8.2 ARM 64
			--
			4.4.3 - 4.4.0, 4.2.16 - 4.2.0, 4.0.26 - 4.0.0, 3.6.22 - 3.6.0, 3.4.23 - 3.4.0, 3.2.21 - 3.2.0, 3.0.14 - 3.0.0, 2.6.12 - 2.6.0
			https://fastdl.mongodb.org/linux/mongodb-linux-aarch64-rhel82-{}.tgz
			5.0.2 - 5.0.0, 4.4.9 - 4.4.4
    */
			PlatformMatchRule centos8arm = PlatformMatchRule.builder()
					.match(DistributionMatch.any(
											VersionRange.of("5.0.0", "5.0.2"),
											VersionRange.of("4.4.0", "4.4.9")
									)
									.andThen(PlatformMatch
											.withOs(OS.Linux)
											.withBitSize(BitSize.B64)
											.withCpuType(CPUType.ARM)
											.withVersion(
													CentosVersion.CentOS_8
											)
									)
					)
					.finder(UrlTemplatePackageResolver.builder()
							.fileSet(fileSet)
							.archiveType(ArchiveType.TGZ)
							.urlTemplate("/linux/mongodb-linux-aarch64-rhel82-{version}.tgz")
							.build())
					.build();

			PlatformMatchRule tools_centos8arm = PlatformMatchRule.builder()
					.match(DistributionMatch.any(
											VersionRange.of("5.0.0", "5.0.2"),
											VersionRange.of("4.4.0", "4.4.9")
									)
									.andThen(PlatformMatch
											.withOs(OS.Linux)
											.withBitSize(BitSize.B64)
											.withCpuType(CPUType.ARM)
											.withVersion(
												CentosVersion.CentOS_8
											)
									)
					)
					.finder(UrlTemplatePackageResolver.builder()
							.fileSet(fileSet)
							.archiveType(ArchiveType.TGZ)
							.urlTemplate("/tools/db/mongodb-database-tools-rhel82-arm64-{tools.version}.tgz")
							.build())
					.build();


			switch (command) {
					case MongoDump:
					case MongoImport:
					case MongoRestore:
							return PlatformMatchRules.empty()
									.withRules(
											tools_centos6, tools_centos7, tools_centos8, tools_centos8arm
									);
			}

    return PlatformMatchRules.empty()
            .withRules(
                    centos6, centos7, centos8, centos8arm
            );
  }
}