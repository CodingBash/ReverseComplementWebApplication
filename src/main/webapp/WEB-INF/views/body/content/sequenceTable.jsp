<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container-fluid white-background" id="sequenceList">
	<table class="table table-hover table-striped">
		<tr>
			<th id="table-sequence-number">Sequence #</th>
			<th id="table-sequence-data">Sequence</th>
			<!-- EDIT -->
			<th id="table-button-first"></th>
			<!-- DELETE -->
			<th id="table-button-second"><button type="button"
					class="btn btn-danger deleteall-modal-appearance center-block" data-toggle="modal"
					data-target="#modal-deleteall-confirmation">DELETE ALL</button></th>
			<th id="table-checkbox">
				<button type="button" class="btn btn-danger delete-selected-button">
					DELETE SELECTED <span class="badge"></span>
				</button>
			</th>
		</tr>
		<!-- TODO: Add type="button" to all buttons -->
		<c:if test="${not empty container}">
			<c:forEach items="${container}" var="sequenceItem" varStatus="iterator">
				<tr class="sequence-element" value="${iterator.index}">
					<td class="sequence-count">#${iterator.index + 1}</td>
					<td class="sequence-string">${sequenceItem}</td>
					<td class="edit-button"><button type="button"
							class="btn btn-info sequence-edit center-block">EDIT</button></td>
					<td class="delete-button">
						<button type="button" class="btn btn-danger delete-modal-appearance center-block"
							data-toggle="modal" data-target="#modal-delete-confirmation">DELETE</button>
					</td>
					<!-- Make checkbox larger, issue centering checkbox -->
					<td><input type="checkbox" class="checkbox-delete-selection center-block" /></td>
				</tr>
			</c:forEach>
		</c:if>
	</table>
</div>