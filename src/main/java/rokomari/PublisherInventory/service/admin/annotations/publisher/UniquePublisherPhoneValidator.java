package rokomari.PublisherInventory.service.admin.annotations.publisher;


import org.springframework.beans.factory.annotation.Autowired;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.service.admin.services.PublisherService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniquePublisherPhoneValidator implements ConstraintValidator <UniquePublisherPhone, Publisher>{

    @Autowired
    private PublisherService publisherService;

    @Override
    public void initialize(UniquePublisherPhone uniquePublisherPhone) {

    }

@Override
    public boolean isValid(Publisher publisher, ConstraintValidatorContext constraintValidatorContext) {

        try{
            if(publisherService.getPublisherByPhone(publisher.getPhone()).getId() == publisher.getId() || publisherService.getPublisherByPhone(publisher.getPhone()) == null){

                System.out.println("Return ID: "+ publisherService.getPublisherByPhone(publisher.getPhone()).getId());

                return true;

                //return false;
            }
            else {

                System.out.println("FALSE, ID: "+ publisherService.getPublisherByPhone(publisher.getPhone()).getId());
                System.out.println("Other ID: "+publisher.getId());

                return false;

                //return true;
            }
        }
        catch (Exception e){

            return true;

            //return false;
        }
    }

}
