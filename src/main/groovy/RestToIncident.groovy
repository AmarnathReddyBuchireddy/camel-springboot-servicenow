import org.apache.camel.servicenow.dto.Incident

return new Incident(shortDescription: request.body['description'], 
                    urgency: request.body['level'], 
                    assignmentGroup: '287ebd7da9fe198100f92cc8d1d2154e', 
                    impact: 2);