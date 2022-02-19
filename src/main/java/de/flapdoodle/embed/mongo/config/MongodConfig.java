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
package de.flapdoodle.embed.mongo.config;

import java.util.Map;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import de.flapdoodle.embed.mongo.packageresolver.Command;
import de.flapdoodle.embed.mongo.config.processlistener.IMongoProcessListener;
import de.flapdoodle.embed.mongo.config.processlistener.NoopProcessListener;
import de.flapdoodle.embed.process.config.SupportConfig;

@Immutable
public interface MongodConfig extends MongoCommonConfig {

	@Default
	default Storage replication() {
		return new Storage();
	}

	@Default
	default boolean isConfigServer() {
		return false;
	}

	@Default
	default boolean isShardServer() {
		return false;
	}

	@Default
	default IMongoProcessListener processListener() {
		return new NoopProcessListener();
	}

	Map<String, String> params();
	
	Map<String, String> args();
	
	@Default
	@Override
	default String pidFile() {
		return "mongod.pid";
	}
	
	@Override
	@Default
	default SupportConfig supportConfig() {
		return new de.flapdoodle.embed.mongo.config.SupportConfig(Command.MongoD);
	}
	
	static ImmutableMongodConfig.Builder builder() {
		return ImmutableMongodConfig.builder();
	}
}
