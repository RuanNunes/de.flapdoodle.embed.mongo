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
package de.flapdoodle.embed.mongo.transitions;

import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.process.archives.ExtractedFileSet;
import de.flapdoodle.embed.process.config.SupportConfig;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.process.types.*;
import de.flapdoodle.os.Platform;
import de.flapdoodle.reverse.State;
import de.flapdoodle.reverse.StateID;
import de.flapdoodle.reverse.StateLookup;
import de.flapdoodle.reverse.Transition;
import de.flapdoodle.reverse.naming.HasLabel;
import org.immutables.value.Value;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Value.Immutable
public class MongosStarter implements Transition<RunningMongosProcess>, HasLabel {

	@Override
	@Value.Default
	public String transitionLabel() {
		return "Start Mongos";
	}

	@Value.Default
	public StateID<ExtractedFileSet> processExecutable() {
		return StateID.of(ExtractedFileSet.class);
	}

	@Value.Default
	public StateID<ProcessConfig> processConfig() {
		return StateID.of(ProcessConfig.class);
	}

	@Value.Default
	public StateID<ProcessEnv> processEnv() {
		return StateID.of(ProcessEnv.class);
	}

	@Value.Default
	public StateID<ProcessArguments> arguments() {
		return StateID.of(ProcessArguments.class);
	}

	@Value.Default
	public StateID<ProcessOutput> processOutput() {
		return StateID.of(ProcessOutput.class);
	}

	@Value.Default
	public StateID<SupportConfig> supportConfig() {
		return StateID.of(SupportConfig.class);
	}

	@Value.Default
	public StateID<Platform> platform() {
		return StateID.of(Platform.class);
	}

	@Value.Default
	public StateID<Net> net() {
		return StateID.of(Net.class);
	}

	@Override
	@Value.Default
	public StateID<RunningMongosProcess> destination() {
		return StateID.of(RunningMongosProcess.class);
	}

	@Override
	public Set<StateID<?>> sources() {
		return StateID.setOf(
			processExecutable(),
			processConfig(),
			processEnv(),
			arguments(),
			processOutput(),
			supportConfig(),
			platform(),
			net()
		);
	}

	@Override
	public State<RunningMongosProcess> result(StateLookup lookup) {
		ExtractedFileSet fileSet = lookup.of(processExecutable());
		List<String> arguments = lookup.of(arguments()).value();
		Map<String, String> environment = lookup.of(processEnv()).value();
		ProcessConfig processConfig = lookup.of(processConfig());
		ProcessOutput processOutput = lookup.of(processOutput());
		SupportConfig supportConfig = lookup.of(supportConfig());
		Platform platform = lookup.of(platform());
		Net net = lookup.of(net());

		try {
			RunningProcessFactory<RunningMongosProcess> factory = RunningMongosProcess.factory(20000, supportConfig, platform, net);

			RunningMongosProcess running = RunningProcess.start(factory, fileSet.executable(), arguments, environment, processConfig,
				processOutput, supportConfig);
			
			return State.of(running, RunningProcess::stop);
		}
		catch (IOException ix) {
			throw new RuntimeException("could not start process", ix);
		}
	}

	public static ImmutableMongosStarter.Builder builder() {
		return ImmutableMongosStarter.builder();
	}

	public static ImmutableMongosStarter withDefaults() {
		return builder().build();
	}
}
