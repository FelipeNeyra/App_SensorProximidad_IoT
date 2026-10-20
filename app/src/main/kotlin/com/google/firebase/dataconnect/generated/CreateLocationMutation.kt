
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



public interface CreateLocationMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      CreateLocationMutation.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val location_insert: LocationKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateLocation"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateLocationMutation.ref(
  
): com.google.firebase.dataconnect.MutationRef<
    CreateLocationMutation.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun CreateLocationMutation.execute(

  

  ): com.google.firebase.dataconnect.MutationResult<
    CreateLocationMutation.Data,
    Unit
  > =
  ref(
    
  ).execute()


