import { AlertCircle } from "lucide-react";

const InputField = ({
  icon: Icon,
  name,
  type = "text",
  label,
  value,
  onChange,
  onBlur,
  onKeyPress,
  error,
  touched,
  ...props
}) => (
  <div className="space-y-2">
    <label htmlFor={name} className="block text-sm font-medium text-gray-700">
      {label}
    </label>

    <div className="relative">
      <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
        <Icon className="h-5 w-5 text-gray-400" />
      </div>

      <input
        id={name}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        onBlur={onBlur}
        onKeyPress={onKeyPress}
        className={`block w-full pl-10 pr-3 py-2.5 border ${
          touched && error
            ? "border-red-300 focus:ring-red-500"
            : "border-gray-300 focus:ring-blue-500"
        } rounded-lg focus:outline-none focus:ring-2 transition-colors`}
        {...props}
      />
    </div>

    {touched && error && (
      <p className="text-sm text-red-600 flex items-center gap-1">
        <AlertCircle className="h-4 w-4" />
        {error}
      </p>
    )}
  </div>
);

export default InputField;
