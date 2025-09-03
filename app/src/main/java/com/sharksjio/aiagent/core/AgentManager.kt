package com.sharksjio.aiagent.core

import kotlinx.coroutines.*

/**
 * Manages multiple AI agents and routes requests to appropriate agents
 */
class AgentManager {
    
    private val agents = mutableMapOf<String, AIAgent>()
    private val contentFilter = ContentFilter()
    
    /**
     * Registers a new agent
     */
    fun registerAgent(agent: AIAgent) {
        agents[agent.config.id] = agent
    }
    
    /**
     * Unregisters an agent
     */
    fun unregisterAgent(agentId: String) {
        agents.remove(agentId)
    }
    
    /**
     * Gets all registered agents
     */
    fun getAllAgents(): List<AIAgent> = agents.values.toList()
    
    /**
     * Gets agents that can handle a specific topic
     */
    fun getAgentsForTopic(topic: TopicCategory): List<AIAgent> {
        return agents.values
            .filter { it.canHandle(topic) && it.isAvailable() }
            .sortedByDescending { it.config.priority }
    }
    
    /**
     * Processes a request using the most appropriate agent(s)
     */
    suspend fun processRequest(request: AgentRequest): AgentResponse {
        // Filter content
        if (!contentFilter.isContentAppropriate(request.content)) {
            return createErrorResponse(request.id, "Content not appropriate for processing")
        }
        
        // Detect topic if not specified or validate specified topic
        val detectedTopic = contentFilter.detectTopic(request.content)
        val finalTopic = if (request.topic == TopicCategory.GENERAL) detectedTopic else request.topic
        
        // Get suitable agents
        val suitableAgents = getAgentsForTopic(finalTopic)
        
        if (suitableAgents.isEmpty()) {
            return createErrorResponse(request.id, "No agents available for topic: $finalTopic")
        }
        
        // Use the highest priority agent
        val selectedAgent = suitableAgents.first()
        
        return try {
            selectedAgent.processRequest(request.copy(topic = finalTopic))
        } catch (e: Exception) {
            createErrorResponse(request.id, "Agent processing failed: ${e.message}")
        }
    }
    
    /**
     * Processes a request using multiple agents and aggregates responses
     */
    suspend fun processRequestWithMultipleAgents(request: AgentRequest, maxAgents: Int = 3): List<AgentResponse> {
        if (!contentFilter.isContentAppropriate(request.content)) {
            return listOf(createErrorResponse(request.id, "Content not appropriate for processing"))
        }
        
        val detectedTopic = contentFilter.detectTopic(request.content)
        val finalTopic = if (request.topic == TopicCategory.GENERAL) detectedTopic else request.topic
        
        val suitableAgents = getAgentsForTopic(finalTopic).take(maxAgents)
        
        if (suitableAgents.isEmpty()) {
            return listOf(createErrorResponse(request.id, "No agents available for topic: $finalTopic"))
        }
        
        return withContext(Dispatchers.IO) {
            suitableAgents.map { agent ->
                async {
                    try {
                        agent.processRequest(request.copy(topic = finalTopic))
                    } catch (e: Exception) {
                        createErrorResponse(request.id, "Agent ${agent.config.name} failed: ${e.message}")
                    }
                }
            }.awaitAll()
        }
    }
    
    private fun createErrorResponse(requestId: String, errorMessage: String): AgentResponse {
        return AgentResponse(
            id = requestId,
            agentId = "system",
            content = errorMessage,
            confidence = 0.0,
            topic = TopicCategory.GENERAL,
            processingTime = 0L,
            metadata = mapOf("error" to true)
        )
    }
}