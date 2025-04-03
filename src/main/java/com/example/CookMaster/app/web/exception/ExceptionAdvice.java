package com.example.CookMaster.app.web.exception;



import com.example.CookMaster.app.exception.DishDoesNotExistsException;
import com.example.CookMaster.app.exception.NotHaveEnoughDishes;
import com.example.CookMaster.app.web.dto.CreateDishRequest;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", exception);
        modelAndView.setViewName("error");

        return modelAndView;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            NotHaveEnoughDishes.class
    })
    public ModelAndView handleNotHaveSufficientDishesException(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("not-sufficient-dishes");

        return modelAndView;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            FeignException.class
    })
    public ModelAndView handleFeignExceptionExceptions(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error-admin-notif");
        return modelAndView;
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({DishDoesNotExistsException.class})
    public ModelAndView handleSearchDishException(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("not-found-dish");
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ModelAndView handleValidationException(BindException ex) {
        if (ex.getBindingResult().getTarget() instanceof CreateDishRequest) {
            ModelAndView modelAndView = new ModelAndView("error-create-dish");
            modelAndView.addObject("errors", ex.getBindingResult().getAllErrors());
            return modelAndView;
        }
        return new ModelAndView("error");
    }
}
