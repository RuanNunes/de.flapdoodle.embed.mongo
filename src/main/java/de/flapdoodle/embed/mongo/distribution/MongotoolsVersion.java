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
package de.flapdoodle.embed.mongo.distribution;

public enum MongotoolsVersion implements de.flapdoodle.embed.process.distribution.Version {

		V100_5_1("100.5.1");

		private final String specificVersion;
		private final NumericVersion numericVersion;

		MongotoolsVersion(String version) {
				this.specificVersion = version;
				this.numericVersion = NumericVersion.of(version);
		}

		@Override
		public String asInDownloadPath() {
				return specificVersion;
		}

		public enum Main implements de.flapdoodle.embed.process.distribution.Version {
				V100_5(MongotoolsVersion.V100_5_1);

				private final MongotoolsVersion dumpVersion;
				Main(MongotoolsVersion dumpVersion) {
						this.dumpVersion = dumpVersion;
				}

				@Override
				public String asInDownloadPath() {
						return dumpVersion.asInDownloadPath();
				}
		}
}
