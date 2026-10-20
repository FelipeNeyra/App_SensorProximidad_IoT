
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.google.firebase.dataconnect.generated



public interface DeleteProjectMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      DeleteProjectMutation.Data,
      DeleteProjectMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val project_delete: ProjectKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "DeleteProject"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun DeleteProjectMutation.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    DeleteProjectMutation.Data,
    DeleteProjectMutation.Variables
  > =
  ref(
    
      DeleteProjectMutation.Variables(
        id=id,
  
      )
    
  )

public suspend fun DeleteProjectMutation.execute(

  
    
      id: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    DeleteProjectMutation.Data,
    DeleteProjectMutation.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute()


