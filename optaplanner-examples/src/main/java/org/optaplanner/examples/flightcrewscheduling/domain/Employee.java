/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.examples.flightcrewscheduling.domain;

import java.util.Set;
import java.util.SortedSet;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;
import org.optaplanner.examples.common.domain.AbstractPersistable;

@PlanningEntity
public class Employee extends AbstractPersistable {

    private String name;
    private Airport homeAirport;

    private Set<Skill> skillSet;

    /**
     * Sorted by {@link FlightAssignment#DATE_TIME_COMPARATOR}.
     */
    @InverseRelationShadowVariable(sourceVariableName = "employee")
    private SortedSet<FlightAssignment> flightAssignmentSet;

    public Employee() {
    }

    public boolean hasSkill(Skill skill) {
        return skillSet.contains(skill);
    }

    public boolean isFirstAssignmentValid() {
        if (flightAssignmentSet.isEmpty()) {
            return true;
        }
        FlightAssignment firstAssignment = flightAssignmentSet.first();
        // TODO allow taking a taxi, but penalize it with a soft score instead
        return firstAssignment.getFlight().getDepartureAirport() == homeAirport;
    }

    public boolean isLastAssignmentValid() {
        if (flightAssignmentSet.isEmpty()) {
            return true;
        }
        FlightAssignment lastAssignment = flightAssignmentSet.last();
        // TODO allow taking a taxi, but penalize it with a soft score instead
        return lastAssignment.getFlight().getArrivalAirport() == homeAirport;
    }

    public long countInvalidConnections() {
        long count = 0L;
        FlightAssignment previousAssignment = null;
        for (FlightAssignment assignment : flightAssignmentSet) {
            if (previousAssignment != null
                    && previousAssignment.getFlight().getArrivalAirport()
                    != assignment.getFlight().getDepartureAirport()) {
                count++;
            }
            previousAssignment = assignment;
        }
        return count;
    }

    @Override
    public String toString() {
        return name;
    }

    // ************************************************************************
    // Simple getters and setters
    // ************************************************************************

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Airport getHomeAirport() {
        return homeAirport;
    }

    public void setHomeAirport(Airport homeAirport) {
        this.homeAirport = homeAirport;
    }

    public Set<Skill> getSkillSet() {
        return skillSet;
    }

    public void setSkillSet(Set<Skill> skillSet) {
        this.skillSet = skillSet;
    }

    public SortedSet<FlightAssignment> getFlightAssignmentSet() {
        return flightAssignmentSet;
    }

    public void setFlightAssignmentSet(SortedSet<FlightAssignment> flightAssignmentSet) {
        this.flightAssignmentSet = flightAssignmentSet;
    }

}
