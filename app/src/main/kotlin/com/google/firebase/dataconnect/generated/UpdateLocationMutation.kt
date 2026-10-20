
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



public interface UpdateLocationMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      UpdateLocationMutation.Data,
      UpdateLocationMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val description: com.google.firebase.dataconnect.OptionalVariable<String?>,
  
  ) {
    
    
      
      @kotlin.DslMarker public annotation class BuilderDsl

      
      @BuilderDsl
      public interface Builder {
        public var id: java.util.UUID
        public var description: String?
        
      }

      public companion object {
        
        @Suppress("NAME_SHADOWING")
        public fun build(
          id: java.util.UUID,
          block_: Builder.() -> Unit
        ): Variables {
          var id= id
            var description: com.google.firebase.dataconnect.OptionalVariable<String?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            

          return object : Builder {
            override var id: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { id = value_ }
              
            override var description: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { description = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            
          }.apply(block_)
          .let {
            Variables(
              id=id,description=description,
            )
          }
        }
      }
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val location_update: LocationKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateLocation"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdateLocationMutation.ref(
  
    id: java.util.UUID,

  
    block_: UpdateLocationMutation.Variables.Builder.() -> Unit = {}
  
): com.google.firebase.dataconnect.MutationRef<
    UpdateLocationMutation.Data,
    UpdateLocationMutation.Variables
  > =
  ref(
    
      UpdateLocationMutation.Variables.build(
        id=id,
  
    block_
      )
    
  )

public suspend fun UpdateLocationMutation.execute(

  
    
      id: java.util.UUID,

  
    block_: UpdateLocationMutation.Variables.Builder.() -> Unit = {}

  ): com.google.firebase.dataconnect.MutationResult<
    UpdateLocationMutation.Data,
    UpdateLocationMutation.Variables
  > =
  ref(
    
      id=id,
  
    block_
    
  ).execute()


