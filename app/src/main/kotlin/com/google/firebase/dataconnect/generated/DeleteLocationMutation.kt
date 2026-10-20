
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



public interface DeleteLocationMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      DeleteLocationMutation.Data,
      DeleteLocationMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val location_delete: LocationKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "DeleteLocation"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun DeleteLocationMutation.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    DeleteLocationMutation.Data,
    DeleteLocationMutation.Variables
  > =
  ref(
    
      DeleteLocationMutation.Variables(
        id=id,
  
      )
    
  )

public suspend fun DeleteLocationMutation.execute(

  
    
      id: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    DeleteLocationMutation.Data,
    DeleteLocationMutation.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute()


