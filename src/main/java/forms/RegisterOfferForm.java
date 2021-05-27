package forms;

import dao.GroupsDAO;
import dao.OffersDAO;
import entity.Group;
import entity.Offer;

import java.util.List;


public class RegisterOfferForm extends Requests implements IRegisterForms {
    protected final OffersDAO offersDAO = new OffersDAO();
    protected final GroupsDAO groupsDAO = new GroupsDAO();


    @Override
    public void registerInDatabase() {
        List<Group> groupsList = groupsDAO.findAllFutureJoinGrCatLocation();                        // get list of future Camps
        groupsDAO.printGroupsList(groupsList);                                                      // print this list

        Offer newOffer = makeInstanceOfOffer();                                                     // make new offer object with chosen (from console) groupID and price
        int groupIdOfGroupToChangeOffer = newOffer.getGroupId();                                    // check groupID of these group, which offer user choose to change

        Offer oldOffer = offersDAO.findActiveByGroupId(groupIdOfGroupToChangeOffer);                // find active offer with the group id from above
        if (oldOffer != null) {
            int oldOfferID = oldOffer.getOfferId();                                                 // if offer exists, get its offer ID
            offersDAO.deactivate(oldOfferID);                                                       // and deactivate it
            System.out.println("Znaleziono i zdeaktywowano starą ofertę.");
        } else {
            System.out.println("Grupa nie miała jeszcze przypisanej aktywnej oferty.");
        }

        offersDAO.save(newOffer);
        int maxOfferId = offersDAO.findMaxOfferId();
        System.out.println("Zapisano nową, aktywną ofertę o ID: " + maxOfferId + " powiązaną z grupą o ID: " + newOffer.getGroupId());
    }

    public void registerInDatabaseWithNewGroup() {
        offersDAO.save(makeInstancesOfGroupAndOffer());
    }

    private Offer makeInstanceOfOffer() {

        Offer offer = new Offer();
        offer.setGroupId(requestGroupId());
        offer.setPrice(requestPrice());

        return offer;
    }

    private Offer makeInstancesOfGroupAndOffer() {
        groupsDAO.save(makeInstanceOfGroup());
        Offer offer = new Offer();
        offer.setGroupId(groupsDAO.findMaxGroupId());
        offer.setPrice(requestPrice());
        return offer;
    }

    private Group makeInstanceOfGroup() {
        Group group = new Group();
        group.setName(requestGroupName());
        group.setCategoriesId(showAndRequestGroupCategory());
        group.setLocationId(showAndRequestLocation());
        group.setAgeRange(requestAgeRange());
        group.setStartDate(requestStartDate());
        group.setEndDate(requestEndDate());
        group.setSize(requestSize());
        return group;
    }

}

