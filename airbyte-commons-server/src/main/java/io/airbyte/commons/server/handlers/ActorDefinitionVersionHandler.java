/*
 * Copyright (c) 2023 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.commons.server.handlers;

import io.airbyte.api.model.generated.ActorDefinitionVersionRead;
import io.airbyte.api.model.generated.DestinationIdRequestBody;
import io.airbyte.api.model.generated.SourceIdRequestBody;
import io.airbyte.config.ActorDefinitionVersion;
import io.airbyte.config.DestinationConnection;
import io.airbyte.config.SourceConnection;
import io.airbyte.config.StandardDestinationDefinition;
import io.airbyte.config.StandardSourceDefinition;
import io.airbyte.config.persistence.ActorDefinitionVersionHelper;
import io.airbyte.config.persistence.ConfigNotFoundException;
import io.airbyte.config.persistence.ConfigRepository;
import io.airbyte.validation.json.JsonValidationException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.IOException;

/**
 * DestinationHandler. Javadocs suppressed because api docs should be used as source of truth.
 */
@SuppressWarnings({"MissingJavadocMethod", "ParameterName"})
@Singleton
public class ActorDefinitionVersionHandler {

  private final ConfigRepository configRepository;
  private final ActorDefinitionVersionHelper actorDefinitionVersionHelper;

  @Inject
  public ActorDefinitionVersionHandler(final ConfigRepository configRepository,
                                       final ActorDefinitionVersionHelper actorDefinitionVersionHelper) {
    this.configRepository = configRepository;
    this.actorDefinitionVersionHelper = actorDefinitionVersionHelper;
  }

  public ActorDefinitionVersionRead getActorDefinitionVersionForSourceId(final SourceIdRequestBody sourceIdRequestBody)
      throws JsonValidationException, ConfigNotFoundException, IOException {
    final SourceConnection sourceConnection = configRepository.getSourceConnection(sourceIdRequestBody.getSourceId());
    final StandardSourceDefinition sourceDefinition = configRepository.getSourceDefinitionFromSource(sourceConnection.getSourceId());
    final ActorDefinitionVersion actorDefinitionVersion =
        actorDefinitionVersionHelper.getSourceVersion(sourceDefinition, sourceConnection.getWorkspaceId(), sourceConnection.getSourceId());
    return createActorDefinitionVersionRead(actorDefinitionVersion);
  }

  public ActorDefinitionVersionRead getActorDefinitionVersionForDestinationId(final DestinationIdRequestBody destinationIdRequestBody)
      throws JsonValidationException, ConfigNotFoundException, IOException {
    final DestinationConnection destinationConnection = configRepository.getDestinationConnection(destinationIdRequestBody.getDestinationId());
    final StandardDestinationDefinition destinationDefinition =
        configRepository.getDestinationDefinitionFromDestination(destinationConnection.getDestinationId());
    final ActorDefinitionVersion actorDefinitionVersion = actorDefinitionVersionHelper.getDestinationVersion(destinationDefinition,
        destinationConnection.getWorkspaceId(), destinationConnection.getDestinationId());
    return createActorDefinitionVersionRead(actorDefinitionVersion);
  }

  private ActorDefinitionVersionRead createActorDefinitionVersionRead(final ActorDefinitionVersion actorDefinitionVersion) {
    return new ActorDefinitionVersionRead()
        .dockerRepository(actorDefinitionVersion.getDockerRepository())
        .dockerImageTag(actorDefinitionVersion.getDockerImageTag());
  }

}
