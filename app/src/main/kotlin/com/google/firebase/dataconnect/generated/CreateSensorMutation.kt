
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



public interface CreateSensorMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      CreateSensorMutation.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val sensor_insert: SensorKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateSensor"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateSensorMutation.ref(
  
): com.google.firebase.dataconnect.MutationRef<
    CreateSensorMutation.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun CreateSensorMutation.execute(

  

  ): com.google.firebase.dataconnect.MutationResult<
    CreateSensorMutation.Data,
    Unit
  > =
  ref(
    
  ).execute()


