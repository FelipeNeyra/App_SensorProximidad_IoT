
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



public interface CreateAlertsMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      CreateAlertsMutation.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val alert_insert: AlertKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateAlerts"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateAlertsMutation.ref(
  
): com.google.firebase.dataconnect.MutationRef<
    CreateAlertsMutation.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun CreateAlertsMutation.execute(

  

  ): com.google.firebase.dataconnect.MutationResult<
    CreateAlertsMutation.Data,
    Unit
  > =
  ref(
    
  ).execute()


