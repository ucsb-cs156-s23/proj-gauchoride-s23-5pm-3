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
    // Stryker restore all

    const navigate = useNavigate();

    return (

        <Form onSubmit={handleSubmit(submitAction)}>

            {initialRideRequest && (
                <>
                    <Form.Group className="mb-3" >
                        <Form.Label htmlFor="id">ID</Form.Label>
                        <Form.Control
                            data-testid="RideRequestForm-id"
                            id="id"
                            type="text"
                            {...register("id")}
                            value={initialRideRequest.id}
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
                            {...register("fullName")}
                            value={initialRideRequest.rider.fullName}
                            disabled
                        />
                    </Form.Group>
                </>
            )}

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="day">Day</Form.Label>
                <Form.Control
                    data-testid="RideRequestForm-day"
                    id="day"
                    type='text'
                    isInvalid={Boolean(errors.day)}
                    {...register("day", { required: "Day is required" })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.day?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="course">Course</Form.Label>
                <Form.Control
                    data-testid="RideRequestForm-course"
                    id="course"
                    type="text"
                    isInvalid={Boolean(errors.localDateTime)}
                    {...register("course", { required: "Course is required" })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.course?.message}
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
                    {errors.startTime?.message}
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
                        required: "Stop Time is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.stopTime?.message}
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
                    {errors.building?.message}
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
                    {errors.room?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="pickupLocation">Pickup Location</Form.Label>
                <Form.Control
                    data-testid="RideRequestForm-pickupLocation"
                    id="pickupLocation"
                    type="text"
                    isInvalid={Boolean(errors.pickupLocation)}
                    {...register("pickupLocation", {
                        required: "Pickup Location is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.pickupLocation?.message}
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
