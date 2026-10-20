
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



public interface DeleteAlertMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      DeleteAlertMutation.Data,
      DeleteAlertMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val alert_delete: AlertKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "DeleteAlert"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun DeleteAlertMutation.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    DeleteAlertMutation.Data,
    DeleteAlertMutation.Variables
  > =
  ref(
    
      DeleteAlertMutation.Variables(
        id=id,
  
      )
    
  )

public suspend fun DeleteAlertMutation.execute(

  
    
      id: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    DeleteAlertMutation.Data,
    DeleteAlertMutation.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute()


