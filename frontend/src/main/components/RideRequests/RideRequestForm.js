import React from 'react';
import { Button, Form } from 'react-bootstrap';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';

function RideRequestForm({ initialRideRequest, submitAction, buttonLabel = "Create" }) {

    // Stryker disable all
    const {
        register,
        formState: { errors },
        handleSubmit,
    } = useForm(
        { defaultValues: initialRideRequest || {}, }
    );
    // Stryker enable all

    const navigate = useNavigate();

    return (

        <Form onSubmit={handleSubmit(submitAction)}>

            {initialRideRequest && (
                <>
                    <Form.Group className="mb-3" >
                        <Form.Label htmlFor="userid">User ID</Form.Label>
                        <Form.Control
                            data-testid="UCSBDateForm-userid"
                            id="userid"
                            type="text"
                            {...register("userid")}
                            value={initialRideRequest.userid}
                            disabled
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" >
                        <Form.Label htmlFor="fullName">Full Name</Form.Label>
                        <Form.Control
                            data-testid="RideRequestForm-fullName"
                            id="fullName"
                            type="text"
                            isInvalid={Boolean(errors.fullName)}
                            {...register("fullName", {
                                required: "Full Name is required."
                            })}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.name?.message}
                        </Form.Control.Feedback>
                    </Form.Group>
                </>
            )}

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="day">Day</Form.Label>
                <Form.Control
                    data-testid="RideRequestForm-day"
                    id="day"
                    type="select"
                    isInvalid={Boolean(errors.day)}
                    {...register("day", { required: true })}
                >
                    <option>Monday</option>
                    <option>Tuesday</option>
                    <option>Wednesday</option>
                    <option>Thursday</option>
                    <option>Friday</option>
                    <option>Saturday</option>
                    <option>Sunday</option>
                </Form.Control>
                <Form.Control.Feedback type="invalid">
                    {errors.day && 'Day is required. '}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="course">Course</Form.Label>
                <Form.Control
                    data-testid="RideRequestForm-course"
                    id="course"
                    type="text"
                    isInvalid={Boolean(errors.localDateTime)}
                    {...register("course", { required: true })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.course && 'Course is required. '}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="startTime">Start Time</Form.Label>
                <Form.Control
                    data-testid="RideRequestForm-startTime"
                    id="startTime"
                    type="text"
                    isInvalid={Boolean(errors.startTime)}
                    {...register("startTime", {
                        required: "Start Time is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.name?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="stopTime">Stop Time</Form.Label>
                <Form.Control
                    data-testid="RideRequestForm-stopTime"
                    id="stopTime"
                    type="text"
                    isInvalid={Boolean(errors.stopTime)}
                    {...register("stopTime", {
                        required: "Full Name is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.name?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="building">Building</Form.Label>
                <Form.Control
                    data-testid="RideRequestForm-building"
                    id="building"
                    type="text"
                    isInvalid={Boolean(errors.building)}
                    {...register("building", {
                        required: "Building is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.name?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="room">Room</Form.Label>
                <Form.Control
                    data-testid="RideRequestForm-room"
                    id="room"
                    type="text"
                    isInvalid={Boolean(errors.room)}
                    {...register("room", {
                        required: "Room is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.name?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="pickup">Pickup Location</Form.Label>
                <Form.Control
                    data-testid="RideRequestForm-pickup"
                    id="pickup"
                    type="text"
                    isInvalid={Boolean(errors.pickup)}
                    {...register("pickup", {
                        required: "Pickup Location is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.name?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Button
                type="submit"
                data-testid="RideRequestForm-submit"
            >
                {buttonLabel}
            </Button>
            <Button
                variant="Secondary"
                onClick={() => navigate(-1)}
                data-testid="RideRequestForm-cancel"
            >
                Cancel
            </Button>

        </Form>

    );
}

export default RideRequestForm;
