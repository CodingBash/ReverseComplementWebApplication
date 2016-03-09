package edu.ilstu.reversecomplementapplication.controllers;

import java.util.Arrays;
import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ilstu.reversecomplementapplication.components.ApplicationUtility;
import edu.ilstu.reversecomplementapplication.models.SequenceContainer;

/**
 * Controller handles server logic in the index.jsp page
 * 
 * @author Bash
 *
 */
@Controller
public class ReverseComplementController
{
	private static final Logger logger = LoggerFactory.getLogger(ReverseComplementController.class);
	private static final String indexPage = "reversecomplement/index";
	
	@Autowired
	ApplicationUtility applicationUtility;

	/**
	 * Empty URL field goes to the index.jsp landing page
	 * 
	 * @return redirect to index.jsp
	 */
	@RequestMapping(value = "/")
	public String getIndex(Model model, HttpSession session)
	{
		// Insert session attribute into generic object
		Object objectedSequenceContainer = session.getAttribute("sequenceContainer");

		// Create a SequenceContainer object
		SequenceContainer sequenceContainer = null;

		// Check if the object is an instance of a SequenceContainer
		if (objectedSequenceContainer instanceof SequenceContainer)
		{
			sequenceContainer = (SequenceContainer) objectedSequenceContainer;
			// Add sequence to the ModelAndView
			model.addAttribute("container", sequenceContainer.getSequenceContainer());
		}

		return indexPage;
	}

	/**
	 * Get the reverse complement of the submitted DNAsequence
	 * 
	 * @param stringSequence
	 *            String of sequence to be submitted
	 * @param model
	 *            object container sent to be used in the view
	 * @return redirect to index.jsp
	 */
	@RequestMapping(value = "/submitSequence.do", method = RequestMethod.POST)
	public String submitSequence(@RequestParam("sequence") String stringSequence, Model model, HttpSession session)
	{
		DNASequence sequence = null;
		try
		{
			sequence = new DNASequence(stringSequence);
		} catch (CompoundNotFoundException e)
		{
			e.printStackTrace();
		}

		if (sequence != null)
		{
			model.addAttribute("oldSequence", sequence.toString());
			try
			{
				sequence = new DNASequence(sequence.getReverseComplement().getSequenceAsString());
			} catch (CompoundNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addAttribute("sequence", sequence.toString());
		} else
		{
			model.addAttribute("sequence", null);
			model.addAttribute("oldSequence", null);
		}

		// Insert session attribute into generic object
		Object objectedSequenceContainer = session.getAttribute("sequenceContainer");

		// Create a SequenceContainer object
		SequenceContainer sequenceContainer = null;

		// Check if the object is an instance of a SequenceContainer
		if (objectedSequenceContainer instanceof SequenceContainer)
		{
			sequenceContainer = (SequenceContainer) objectedSequenceContainer;
			// Add sequence to the ModelAndView
			model.addAttribute("container", sequenceContainer.getSequenceContainer());
		}
		return indexPage;
	}

	/**
	 * Save sequence in server container
	 * 
	 * @param stringSequence
	 *            String of sequence to be saved
	 * @param model
	 *            object container sent to be used in the view
	 * @param session
	 *            Spring session object
	 * @return redirect to index.jsp
	 */
	@RequestMapping(value = "/saveSequence.do", method = RequestMethod.POST)
	public String saveSequence(@RequestParam("sequence") String stringSequence, Model model, HttpSession session)
	{
		// Insert session attribute into generic object
		Object objectedSequenceContainer = session.getAttribute("sequenceContainer");

		// Create a SequenceContainer object
		SequenceContainer sequenceContainer = null;

		// Check if the object is an instance of a SequenceContainer
		if (objectedSequenceContainer instanceof SequenceContainer)
		{
			sequenceContainer = (SequenceContainer) objectedSequenceContainer;
		}
		// If not, make a new SequenceContainer
		else
		{
			sequenceContainer = new SequenceContainer(new LinkedList<AbstractSequence<NucleotideCompound>>());
		}

		// Create the sequence from the @RequestParam
		AbstractSequence<NucleotideCompound> sequence = null;
		try
		{
			sequence = new DNASequence(stringSequence);
		} catch (CompoundNotFoundException e)
		{
			e.printStackTrace();
			// TODO: Error handling
		}

		// Add the DNASequence
		sequenceContainer.addSequenceToContainer(sequence);

		// Add updated sequenceContainer back to session
		session.setAttribute("sequenceContainer", sequenceContainer);

		// Add sequence to the ModelAndView
		model.addAttribute("container", sequenceContainer.getSequenceContainer());

		// Refresh the index page
		return indexPage;
	}

	/**
	 * Edit sequence in server container
	 * 
	 * @param stringSequence
	 *            String of sequence to be saved
	 * @param model
	 *            object container sent to be used in the view
	 * @param session
	 *            Spring session object
	 * @return redirect to index.jsp
	 */// TODO: Error handling.
	@RequestMapping(value = "/editSequence.do", method = RequestMethod.POST)
	public String editSequence(@RequestParam("sequence") String stringSequence, @RequestParam("index") int index,
			Model model, HttpSession session)
	{

		// Insert session attribute into generic object
		Object objectedSequenceContainer = session.getAttribute("sequenceContainer");

		// Create a SequenceContainer object
		SequenceContainer sequenceContainer = null;

		// Check if the object is an instance of a SequenceContainer
		if (objectedSequenceContainer instanceof SequenceContainer)
		{

			sequenceContainer = (SequenceContainer) objectedSequenceContainer;
		}
		// If not, make a new SequenceContainer
		else
		{
			sequenceContainer = new SequenceContainer(new LinkedList<AbstractSequence<NucleotideCompound>>());
		}

		// Create the sequence from the @RequestParam
		AbstractSequence<NucleotideCompound> sequence = null;
		try
		{
			sequence = new DNASequence(stringSequence);
			// Edit the sequence
			sequenceContainer.editSequenceInContainer(index, sequence);
		} catch (CompoundNotFoundException e)
		{
			e.printStackTrace();

		}

		// Add updated sequenceContainer back to session
		session.setAttribute("sequenceContainer", sequenceContainer);

		// Add sequence to the ModelAndView
		model.addAttribute("container", sequenceContainer.getSequenceContainer());

		// Refresh the index page
		return indexPage;
	}

	/**
	 * Delete sequence in server container
	 * 
	 * @param model
	 *            object container sent to be used in the view
	 * @param session
	 *            Spring session object
	 * @return redirect to index.jsp
	 */// TODO: Don't delete, validate request
	@RequestMapping(value = "/deleteSequence.do", method = RequestMethod.POST)
	public String deleteSequence(@RequestParam("index") int index, Model model, HttpSession session)
	{
		// Insert session attribute into generic object
		Object objectedSequenceContainer = session.getAttribute("sequenceContainer");

		// Create a SequenceContainer object
		SequenceContainer sequenceContainer = null;

		// Check if the object is an instance of a SequenceContainer
		if (objectedSequenceContainer instanceof SequenceContainer)
		{

			sequenceContainer = (SequenceContainer) objectedSequenceContainer;
		}
		// If not, make a new SequenceContainer
		else
		{
			sequenceContainer = new SequenceContainer(new LinkedList<AbstractSequence<NucleotideCompound>>());
		}

		// Delete the sequence
		try
		{
			sequenceContainer.removeSequenceInContainer(index);
		} catch (IndexOutOfBoundsException ioobe)
		{
			ioobe.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		// Add updated sequenceContainer back to session
		session.setAttribute("sequenceContainer", sequenceContainer);

		// Add sequence to the ModelAndView
		model.addAttribute("container", sequenceContainer.getSequenceContainer());

		// Refresh the index page
		return indexPage;
	}

	/**
	 * Delete all sequences in server container
	 * 
	 * @param model
	 *            object container sent to be used in the view
	 * @param session
	 *            Spring session object
	 * @return redirect to index.jsp
	 */
	@RequestMapping(value = "/deleteAllSequences.do", method = RequestMethod.POST)
	public String deleteAllSequences(Model model, HttpSession session)
	{
		// Insert session attribute into generic object
		Object objectedSequenceContainer = session.getAttribute("sequenceContainer");

		// Create a SequenceContainer object
		SequenceContainer sequenceContainer = null;

		// Check if the object is an instance of a SequenceContainer
		if (objectedSequenceContainer instanceof SequenceContainer)
		{

			sequenceContainer = (SequenceContainer) objectedSequenceContainer;
		}
		// If not, make a new SequenceContainer
		else
		{
			sequenceContainer = new SequenceContainer(new LinkedList<AbstractSequence<NucleotideCompound>>());
		}

		// Delete the sequence
		try
		{
			sequenceContainer.removeAllSequencesInContainer();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		// Add updated sequenceContainer back to session
		session.setAttribute("sequenceContainer", sequenceContainer);

		// Add sequence to the ModelAndView
		model.addAttribute("container", sequenceContainer.getSequenceContainer());

		// Refresh the index page
		return indexPage;
	}

	/**
	 * Delete a list of selected indexes from the list of sequences
	 * 
	 * @param stringIndexList
	 *            list of indexes to remove
	 * @param model
	 *            object container sent to be used in the view
	 * @param session
	 *            Spring session object
	 * @return redirect to index.jsp
	 */
	@RequestMapping(value = "/deleteSelectedSequences.do", method = RequestMethod.POST)
	public String deleteSelectedSequences(@RequestParam("indexList") String[] stringIndexList, Model model,
			HttpSession session)
	{
		// Insert session attribute into generic object
		Object objectedSequenceContainer = session.getAttribute("sequenceContainer");

		// Create a SequenceContainer object
		SequenceContainer sequenceContainer = null;

		// Check if the object is an instance of a SequenceContainer
		if (objectedSequenceContainer instanceof SequenceContainer)
		{

			sequenceContainer = (SequenceContainer) objectedSequenceContainer;
		}
		// If not, make a new SequenceContainer
		else
		{
			sequenceContainer = new SequenceContainer(new LinkedList<AbstractSequence<NucleotideCompound>>());
		}

		// Convert String[] to int[]
		int[] intIndexList = applicationUtility.convertStringArrayToIntArray(stringIndexList);

		// Delete the sequences
		try
		{
			sequenceContainer.removeSelectedSequencesInContainer(intIndexList);
		} catch (IndexOutOfBoundsException ioobe)
		{
			ioobe.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		// Add updated sequenceContainer back to session
		session.setAttribute("sequenceContainer", sequenceContainer);

		// Add sequence to the ModelAndView
		model.addAttribute("container", sequenceContainer.getSequenceContainer());

		// Refresh the index page
		return indexPage;
	}
}