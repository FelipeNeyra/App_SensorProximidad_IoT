
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



public interface CreateProjectMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      CreateProjectMutation.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val project_insert: ProjectKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateProject"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateProjectMutation.ref(
  
): com.google.firebase.dataconnect.MutationRef<
    CreateProjectMutation.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun CreateProjectMutation.execute(

  

  ): com.google.firebase.dataconnect.MutationResult<
    CreateProjectMutation.Data,
    Unit
  > =
  ref(
    
  ).execute()


